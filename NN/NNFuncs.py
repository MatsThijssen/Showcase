#Backbone of NN, functions are called by NN.py Dropout parameter is set here as well

import os
import glob
import math
import numpy as np
import matplotlib.pyplot as plt
from matplotlib import cm
from matplotlib import gridspec
import pandas as pd
from sklearn import metrics
import tensorflow as tf
from tensorflow.python.data import Dataset
import time
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '1'



#Choose what features we want to play with, by adding or removing names in array
def preprocess_features(ATLAS_DF):
  
    selected_features = ATLAS_DF[["jet_n","jet1_pt","jet2_pt","jet3_pt","met","met_phi","HT","HT_Sig","Meff","DPhi1","DPhi2","event_weight"]]
    
    return selected_features

#Target/Label is whether event is signal or not
def preprocess_targets(ATLAS_DF):
  
    output_targets = pd.DataFrame()
    output_targets["Is_Signal"] = ATLAS_DF["Is_Signal"]
    return output_targets


#Constructs feature columns necessary in TF. Takes a dataframe as argument. 
def construct_feature_columns(input_features):
    feature_set = set()
    for my_feature in input_features:
        if (my_feature!='event_weight') and (my_feature!='met_phi'): #Do not want to use these to classify on!
            feature_set.add(tf.feature_column.numeric_column(my_feature))

    return feature_set





def my_input_fn(features, targets, batch_size=1, shuffle=True, num_epochs=None):
   
    
    # Convert pandas data into a dict of np arrays.
    features = {key:np.array(value) for key,value in dict(features).items()}                                             
 
    # Construct a dataset, and configure batching/repeating.
    ds = Dataset.from_tensor_slices((features,targets))
    ds = ds.batch(batch_size).repeat(num_epochs)
    
    #Return the next batch of data.
    features, labels = ds.make_one_shot_iterator().get_next()
    return ds

    
    


#Actual training and testing done here
def train_nn_classification_model(
    learning_rate,
    steps,  
    batch_size,
    hidden_units,
    cuts,
    training_examples,
    training_targets,
    Val700_examples,
    Val700_targets,
    Test700_examples,
    Test700_targets,
    Val800_examples,
    Val800_targets,
    Test800_examples,
    Test800_targets):
  
#Set seed based on met_phi  
    tf.set_random_seed(int(abs(100*training_examples['met_phi'].mean())+1))

  
  # Create a DNNClassifier object.
  # Use Adagrad, and event weights to weight loss appropriately. 
    my_optimizer = tf.train.AdagradOptimizer(learning_rate=learning_rate)
    my_optimizer = tf.contrib.estimator.clip_gradients_by_norm(my_optimizer, 5.0)
    weight_column = tf.feature_column.numeric_column('event_weight')
    classifier = tf.estimator.DNNClassifier(
        feature_columns=construct_feature_columns(training_examples),
        n_classes=2,
        hidden_units=hidden_units,
        weight_column=weight_column,
        optimizer=my_optimizer,
        config=tf.contrib.learn.RunConfig(keep_checkpoint_max=1),
        model_dir="",
        dropout=0.1
    )
  
    

    

  # Train the model
    print("Training model...")
    #Get time to see how long different parts of NN take
    start_training_time = time.time()
    Train_predictions = np.empty(0)
    Train_probabilities = np.empty(0)
    
    for i in range (0, cuts): #Train in cuts

        if ((i+1) == cuts):
            end = None
        else:
            end = int((len(training_examples) / cuts) * (i+1))
        start = int((len(training_examples) / cuts) * (i))
        
        #Create input function
        training_input_fn = lambda: my_input_fn(training_examples[start:end], 
                                          training_targets["Is_Signal"][start:end], 
                                          batch_size=batch_size)
        #Do the actual training
        classifier.train(
            input_fn=training_input_fn,
            steps=steps
        )
        #Predict on the training samples to get metrics
        predict_training_input_fn = lambda: my_input_fn(training_examples[start:end], 
                                                    training_targets["Is_Signal"][start:end], 
                                                    num_epochs=1, 
                                                    shuffle=False)
        
        #As training is done in cuts, must concatenate all the outputs together
        Train_predictions_gen = classifier.predict(input_fn=predict_training_input_fn)
        Train_predictions_temp = np.array([item['class_ids'][0] for item in Train_predictions_gen])
        Train_predictions_gen = classifier.predict(input_fn=predict_training_input_fn)
        Train_probabilities_temp = np.array([item['probabilities'][1] for item in Train_predictions_gen])
        Train_predictions = np.concatenate((Train_predictions,Train_predictions_temp))
        Train_probabilities = np.concatenate((Train_probabilities,Train_probabilities_temp))
    
    #Finished training section, get time
    print("Model training finished. (",time.time()-start_training_time," sec)")
    # Remove event files to save disk space.
    _ = map(os.remove, glob.glob(os.path.join(classifier.model_dir, 'events.out.tfevents*')))

    #Calculate some metrics for training data
    Train_accuracy = metrics.accuracy_score(training_targets, Train_predictions)
    Train_logloss = metrics.log_loss(training_targets, Train_predictions)
    Train_AUC = metrics.roc_auc_score(training_targets,Train_probabilities)
    Train_precision = metrics.precision_score(training_targets, Train_predictions)
    Train_recall = metrics.recall_score(training_targets, Train_predictions)
    Train_fpr, Train_tpr, Train_thresholds = metrics.roc_curve(training_targets,Train_probabilities)

    
    #Arrays to hold predictions and probs for val/test 
    Test700_predictions = np.empty(0)
    Test700_probabilities = np.empty(0)
    
    Val700_predictions = np.empty(0)
    Val700_probabilities = np.empty(0)
    
    Test800_predictions = np.empty(0)
    Test800_probabilities = np.empty(0)
    
    Val800_predictions = np.empty(0)
    Val800_probabilities = np.empty(0)
    
    
    ######### 700 Signal, same as training predictions
    for i in range (0, cuts):
        
        if ((i+1) == cuts):
            end = None
        else:
            end = int((len(Val700_examples) / cuts) * (i+1))
        start = int((len(Val700_examples) / cuts) * (i))
        
        predict_validation_input_fn = lambda: my_input_fn(Val700_examples[start:end], 
                                                    Val700_targets["Is_Signal"][start:end], 
                                                    num_epochs=1, 
                                                    shuffle=False)
        
        Val700_predictions_gen = classifier.predict(input_fn=predict_validation_input_fn)
        Val700_predictions_temp = np.array([item['class_ids'][0] for item in Val700_predictions_gen])
        Val700_predictions_gen = classifier.predict(input_fn=predict_validation_input_fn)
        Val700_probabilities_temp = np.array([item['probabilities'][1] for item in Val700_predictions_gen])
        Val700_predictions = np.concatenate((Val700_predictions,Val700_predictions_temp))
        Val700_probabilities = np.concatenate((Val700_probabilities,Val700_probabilities_temp))
        
        predict_test_input_fn = lambda: my_input_fn(Test700_examples[start:end], 
                                                    Test700_targets["Is_Signal"][start:end], 
                                                    num_epochs=1, 
                                                    shuffle=False)
        
        Test700_predictions_gen = classifier.predict(input_fn=predict_test_input_fn)
        Test700_predictions_temp = np.array([item['class_ids'][0] for item in Test700_predictions_gen])
        Test700_predictions_gen = classifier.predict(input_fn=predict_test_input_fn)
        Test700_probabilities_temp = np.array([item['probabilities'][1] for item in Test700_predictions_gen])
        Test700_predictions = np.concatenate((Test700_predictions,Test700_predictions_temp))
        Test700_probabilities = np.concatenate((Test700_probabilities,Test700_probabilities_temp))

    #Calculate metrics for validations and testing
    Val700_accuracy = metrics.accuracy_score(Val700_targets, Val700_predictions)
    Val700_logloss = metrics.log_loss(Val700_targets, Val700_predictions)
    Val700_AUC = metrics.roc_auc_score(Val700_targets,Val700_probabilities)
    Val700_precision = metrics.precision_score(Val700_targets, Val700_predictions)
    Val700_recall = metrics.recall_score(Val700_targets, Val700_predictions)
    Val700_fpr, Val700_tpr, Val700_thresholds = metrics.roc_curve(Val700_targets,Val700_probabilities)
    
    Test700_accuracy = metrics.accuracy_score(Test700_targets, Test700_predictions)
    Test700_logloss = metrics.log_loss(Test700_targets, Test700_predictions)
    Test700_AUC = metrics.roc_auc_score(Test700_targets,Test700_probabilities)
    Test700_precision = metrics.precision_score(Test700_targets, Test700_predictions)
    Test700_recall = metrics.recall_score(Test700_targets, Test700_predictions)
    Test700_fpr, Test700_tpr, Test700_thresholds = metrics.roc_curve(Test700_targets,Test700_probabilities)
    
    ############################### 800 - Signal, same as before
    
    for i in range (0, cuts):
        
        if ((i+1) == cuts):
            end = None
        else:
            end = int((Val800_examples.size / cuts) * (i+1))
        start = int((Val800_examples.size / cuts) * (i))
        
        predict_validation_input_fn = lambda: my_input_fn(Val800_examples[start:end], 
                                                    Val800_targets["Is_Signal"][start:end], 
                                                    num_epochs=1, 
                                                    shuffle=False)
        
        Val800_predictions_gen = classifier.predict(input_fn=predict_validation_input_fn)
        Val800_predictions_temp = np.array([item['class_ids'][0] for item in Val800_predictions_gen])
        Val800_predictions_gen = classifier.predict(input_fn=predict_validation_input_fn)
        Val800_probabilities_temp = np.array([item['probabilities'][1] for item in Val800_predictions_gen])
        Val800_predictions = np.concatenate((Val800_predictions,Val800_predictions_temp))
        Val800_probabilities = np.concatenate((Val800_probabilities,Val800_probabilities_temp))
        
        predict_test_input_fn = lambda: my_input_fn(Test800_examples[start:end], 
                                                    Test800_targets["Is_Signal"][start:end], 
                                                    num_epochs=1, 
                                                    shuffle=False)
        
        Test800_predictions_gen = classifier.predict(input_fn=predict_test_input_fn)
        Test800_predictions_temp = np.array([item['class_ids'][0] for item in Test800_predictions_gen])
        Test800_predictions_gen = classifier.predict(input_fn=predict_test_input_fn)
        Test800_probabilities_temp = np.array([item['probabilities'][1] for item in Test800_predictions_gen])
        Test800_predictions = np.concatenate((Test800_predictions,Test800_predictions_temp))
        Test800_probabilities = np.concatenate((Test800_probabilities,Test800_probabilities_temp))


    Val800_accuracy = metrics.accuracy_score(Val800_targets, Val800_predictions)
    Val800_logloss = metrics.log_loss(Val800_targets, Val800_predictions)
    Val800_AUC = metrics.roc_auc_score(Val800_targets,Val800_probabilities)
    Val800_precision = metrics.precision_score(Val800_targets, Val800_predictions)
    Val800_recall = metrics.recall_score(Val800_targets, Val800_predictions)
    Val800_fpr, Val800_tpr, Val800_thresholds = metrics.roc_curve(Val800_targets,Val800_probabilities)
    
    Test800_accuracy = metrics.accuracy_score(Test800_targets, Test800_predictions)
    Test800_logloss = metrics.log_loss(Test800_targets, Test800_predictions)
    Test800_AUC = metrics.roc_auc_score(Test800_targets,Test800_probabilities)
    Test800_precision = metrics.precision_score(Test800_targets, Test800_predictions)
    Test800_recall = metrics.recall_score(Test800_targets, Test800_predictions)
    Test800_fpr, Test800_tpr, Test800_thresholds = metrics.roc_curve(Test800_targets,Test800_probabilities)
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    #Output various metrics in one file 
    pwd = os.getcwd()
    os.chdir('NNOutputFiles')
    head = "Metric   | Training | Validation (700) | Testing (700) | Validation (800) | Testing (800) "
    acc = "Accuracy | %-8.3f | %-16.3f | %-16.3f | %-16.3f | %-16.3f" % (Train_accuracy, Val700_accuracy, Test700_accuracy, Val800_accuracy, Test800_accuracy)
    loss = "LogLoss  | %-8.3f | %-16.3f | %-16.3f | %-16.3f | %-16.3f" % (Train_logloss, Val700_logloss, Test700_logloss, Val800_logloss, Test800_logloss)
    auc = "AUC      | %-8.3f | %-16.3f | %-16.3f | %-16.3f | %-16.3f" % (Train_AUC, Val700_AUC, Test700_AUC, Val800_AUC, Test800_AUC)
    prec = "Precision| %-8.3f | %-16.3f | %-16.3f | %-16.3f | %-16.3f" % (Train_precision, Val700_precision, Test700_precision, Val800_precision, Test800_precision)
    rec = "Recall   | %-8.3f | %-16.3f | %-16.3f | %-16.3f | %-16.3f" % (Train_recall, Val700_recall, Test700_recall, Val800_recall, Test800_recall)
    np.savetxt("Info.txt",[head,acc,loss,auc,prec,rec],fmt='%s')
    #np.savetxt("Info"+str(RunNr)+".txt",[Train_accuracy, Val_accuracy, Train_logloss, Val_logloss, Train_AUC, Val_AUC, Train_precision, Val_precision, Train_recall, Val_recall], fmt='%.3f')
    os.chdir(pwd)
    print("Done predicting: ",time.time()-start_training_time," sec)")
    
   
    Val700_cm = metrics.confusion_matrix(Val700_targets, Val700_predictions)
    Test700_cm = metrics.confusion_matrix(Test700_targets, Test700_predictions)
    Val800_cm = metrics.confusion_matrix(Val800_targets, Val800_predictions)
    Test800_cm = metrics.confusion_matrix(Test800_targets, Test800_predictions)

    return classifier, Val700_predictions, Val700_probabilities, Val700_cm, Val700_fpr, Val700_tpr, Val700_thresholds, Test700_predictions, Test700_probabilities, Test700_cm, Test700_fpr, Test700_tpr, Test700_thresholds, Val800_predictions, Val800_probabilities, Val800_cm, Val800_fpr, Val800_tpr, Val800_thresholds, Test800_predictions, Test800_probabilities, Test800_cm, Test800_fpr, Test800_tpr, Test800_thresholds
