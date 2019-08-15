#Main code that runs NN. Parameters of NN are set here. Saves raw data from NN as well as Confusion matrices and more info. Uses functions from NNFuncs.py

import numpy as np
import pandas as pd
import tensorflow as tf
from tensorflow.python.data import Dataset
import NNFuncs as NN
import time
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '1'
pd.options.display.max_rows = 10
pd.options.display.float_format = '{:.3f}'.format



#Get starting time, for keeping track of runtime
start_time = time.time()
print("Start at: ", time.ctime(start_time))

#Import datasets, one for training, 2 per scenario for validation and testing
Training_DF = pd.read_pickle('../../Training_DF.pkl')
Val700_DF = pd.read_pickle('../../Val700_DF.pkl')
Test700_DF = pd.read_pickle('../../Test700_DF.pkl')
Val800_DF = pd.read_pickle('../../Val800_DF.pkl')
Test800_DF = pd.read_pickle('../../Test800_DF.pkl')

#Get intermediate time-stamp
print("Done Importing after :", time.time()-start_time, " seconds")


#Split into training, validation, and testing
training_examples = NN.preprocess_features(Training_DF)
training_targets = NN.preprocess_targets(Training_DF)

Val700_examples = NN.preprocess_features(Val700_DF)
Val700_targets = NN.preprocess_targets(Val700_DF)
Val800_examples = NN.preprocess_features(Val800_DF)
Val800_targets = NN.preprocess_targets(Val800_DF)

Test700_examples = NN.preprocess_features(Test700_DF)
Test700_targets = NN.preprocess_targets(Test700_DF)
Test800_examples = NN.preprocess_features(Test800_DF)
Test800_targets = NN.preprocess_targets(Test800_DF)

#Directory to house output files: Metrics, CMs, etc.
os.mkdir('NNOutputFiles')

#Set seed based on met_phi
tf.set_random_seed(int(abs(100*training_examples['met_phi'].mean())+1))

#Run Neural Network, outputs a bunch of arrays for analysis
classifier, Val700_predictions, Val700_probabilities, Val700_cm, Val700_fpr, Val700_tpr, Val700_thresholds, Test700_predictions, Test700_probabilities, Test700_cm, Test700_fpr, Test700_tpr, Test700_thresholds, Val800_predictions, Val800_probabilities, Val800_cm, Val800_fpr, Val800_tpr, Val800_thresholds, Test800_predictions, Test800_probabilities, Test800_cm, Test800_fpr, Test800_tpr, Test800_thresholds = NN.train_nn_classification_model(
    learning_rate=0.03,
    steps=1500,
    batch_size=50000,
    hidden_units=[100, 200, 100],
    cuts = 20,
    training_examples=training_examples,
    training_targets=training_targets,
    Val700_examples=Val700_examples,
    Val700_targets=Val700_targets,
    Test700_examples=Test700_examples,
    Test700_targets=Test700_targets,
    Val800_examples=Val800_examples,
    Val800_targets=Val800_targets,
    Test800_examples=Test800_examples,
    Test800_targets=Test800_targets)


#Save all info in case needed later
os.chdir('NNOutputFiles')
np.savetxt("Val700Predictions.txt",Val700_predictions)
np.savetxt("Val700ConfusionMatrix.txt",Val700_cm)
np.savetxt("Val700FPR.txt",Val700_fpr)
np.savetxt("Val700TPR.txt",Val700_tpr)
np.savetxt("Val700Probabilities.txt",Val700_probabilities)
np.savetxt("Val700Thresholds.txt",Val700_thresholds)

np.savetxt("Test700Predictions.txt",Test700_predictions)
np.savetxt("Test700ConfusionMatrix.txt",Test700_cm)
np.savetxt("Test700FPR.txt",Test700_fpr)
np.savetxt("Test700TPR.txt",Test700_tpr)
np.savetxt("Test700Probabilities.txt",Test700_probabilities)
np.savetxt("Test700Thresholds.txt",Test700_thresholds)

np.savetxt("Val800Predictions.txt",Val800_predictions)
np.savetxt("Val800ConfusionMatrix.txt",Val800_cm)
np.savetxt("Val800FPR.txt",Val800_fpr)
np.savetxt("Val800TPR.txt",Val800_tpr)
np.savetxt("Val800Probabilities.txt",Val800_probabilities)
np.savetxt("Val800Thresholds.txt",Val800_thresholds)

np.savetxt("Test800Predictions.txt",Test800_predictions)
np.savetxt("Test800ConfusionMatrix.txt",Test800_cm)
np.savetxt("Test800FPR.txt",Test800_fpr)
np.savetxt("Test800TPR.txt",Test800_tpr)
np.savetxt("Test800Probabilities.txt",Test800_probabilities)
np.savetxt("Test800Thresholds.txt",Test800_thresholds)


#Create Confusion Matrices, holding [[TN, FP],[FN,TP]]
RealVal700CM = np.zeros([2,2],dtype=float)
Val700Weights = (150.0/36.0)*np.asarray(Val700_examples['event_weight']) #Factor for scaling up integrated luminosity
for i in range(Val700_predictions.size):
    weight = Val700Weights[i]
    
    if (Val700_predictions[i]==np.asarray(Val700_targets['Is_Signal'])[i]):
        if (Val700_predictions[i]==0):
            RealVal700CM[0][0]+=weight
        else:
            RealVal700CM[1][1]+=weight
    else:
        if (Val700_predictions[i]==0):
            RealVal700CM[1][0]+=weight
        else:
            RealVal700CM[0][1]+=weight

np.savetxt("WeightedCMVal700.txt",RealVal700CM, '%6.3f')

#Compute and print significance 
ValSig = RealVal700CM[1][1]/np.sqrt(RealVal700CM[1][1]+1.06*RealVal700CM[0][1])
print( 'Significance on validation (700-695) is: %5.3f' % ValSig)

RealTest700CM = np.zeros([2,2],dtype=float)
Test700Weights = (150.0/36.0)*np.asarray(Test700_examples['event_weight'])
for i in range(Test700_predictions.size):
    weight = Test700Weights[i]
    
    if (Test700_predictions[i]==np.asarray(Test700_targets['Is_Signal'])[i]):
        if (Test700_predictions[i]==0):
            RealTest700CM[0][0]+=weight
        else:
            RealTest700CM[1][1]+=weight
    else:
        if (Test700_predictions[i]==0):
            RealTest700CM[1][0]+=weight
        else:
            RealTest700CM[0][1]+=weight

np.savetxt("WeightedCMTest700.txt",RealTest700CM, '%6.3f')
TestSig = RealTest700CM[1][1]/np.sqrt(RealTest700CM[1][1]+1.06*RealTest700CM[0][1])
print( 'Significance on testing (700-695) is: %5.3f' % TestSig)

####### 800-Sig: 
RealVal800CM = np.zeros([2,2],dtype=float)
Val800Weights = (150.0/36.0)*np.asarray(Val800_examples['event_weight'])
for i in range(Val800_predictions.size):
    weight = Val800Weights[i]
    
    if (Val800_predictions[i]==np.asarray(Val800_targets['Is_Signal'])[i]):
        if (Val800_predictions[i]==0):
            RealVal800CM[0][0]+=weight
        else:
            RealVal800CM[1][1]+=weight
    else:
        if (Val800_predictions[i]==0):
            RealVal800CM[1][0]+=weight
        else:
            RealVal800CM[0][1]+=weight

np.savetxt("WeightedCMVal800.txt",RealVal800CM, '%6.3f')
ValSig = RealVal800CM[1][1]/np.sqrt(RealVal800CM[1][1]+1.06*RealVal800CM[0][1])
print( 'Significance on validation (800-795) is: %5.3f' % ValSig)

RealTest800CM = np.zeros([2,2],dtype=float)
Test800Weights = (150.0/36.0)*np.asarray(Test800_examples['event_weight'])
for i in range(Test800_predictions.size):
    weight = Test800Weights[i]
    
    if (Test800_predictions[i]==np.asarray(Test800_targets['Is_Signal'])[i]):
        if (Test800_predictions[i]==0):
            RealTest800CM[0][0]+=weight
        else:
            RealTest800CM[1][1]+=weight
    else:
        if (Test800_predictions[i]==0):
            RealTest800CM[1][0]+=weight
        else:
            RealTest800CM[0][1]+=weight

np.savetxt("WeightedCMTest800.txt",RealTest800CM, '%6.3f')
TestSig = RealTest800CM[1][1]/np.sqrt(RealTest800CM[1][1]+1.06*RealTest800CM[0][1])
print( 'Significance on testing (800-795) is: %5.3f' % TestSig)

#Get final time 
print("Done running NN after :", time.time()-start_time, " seconds")






