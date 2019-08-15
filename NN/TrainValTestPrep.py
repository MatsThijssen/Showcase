#Script that combines signal and bg into DFs, as well as normalizes them. Returns One training DF (700 and 800 signal), and validation and testing for both regions separately

import pandas as pd
import numpy as np
import math
import time

start_time = time.time()
print('Starting at: ', time.ctime(start_time))

#Import All data
S700DF = pd.read_pickle('RoughCutData/Signal700DF.pkl')
S800DF = pd.read_pickle('RoughCutData/Signal800DF.pkl')

WDF = pd.read_hdf('WDF.h5','df')
ZDF = pd.read_hdf('ZDF.h5','df')

np.random.seed(seed = int(abs(100*ZDF['met_phi'].mean())+1)) #Set random seed
#Randomize them first
S700DF = S700DF.reindex(
    np.random.permutation(S700DF.index))
S800DF = S800DF.reindex(
    np.random.permutation(S800DF.index))
WDF = WDF.reindex(
    np.random.permutation(WDF.index))
ZDF = ZDF.reindex(
    np.random.permutation(ZDF.index))



#Pick ~60% of each for training. 20% for val, 20% for test.
S700Train = S700DF[:int(0.6*S700DF.shape[0])].copy()
S700Val = S700DF[int(0.6*S700DF.shape[0]):int(0.8*S700DF.shape[0])].copy()
S700Test = S700DF[int(0.8*S700DF.shape[0]):].copy()

S800Train = S800DF[:int(0.6*S800DF.shape[0])].copy()
S800Val = S800DF[int(0.6*S800DF.shape[0]):int(0.8*S800DF.shape[0])].copy()
S800Test = S800DF[int(0.8*S800DF.shape[0]):].copy()

WTrain = WDF[:int(0.6*WDF.shape[0])].copy()
WVal = WDF[int(0.6*WDF.shape[0]):int(0.8*WDF.shape[0])].copy()
WTest = WDF[int(0.8*WDF.shape[0]):].copy()

ZTrain = ZDF[:int(0.6*ZDF.shape[0])].copy()
ZVal = ZDF[int(0.6*ZDF.shape[0]):int(0.8*ZDF.shape[0])].copy()
ZTest = ZDF[int(0.8*ZDF.shape[0]):].copy()


#Bost event weight in signal training samples, both boosted equally
SignalBoost = 500
S700Train['event_weight'] = SignalBoost*S700Train['event_weight']
S800Train['event_weight'] = SignalBoost*S800Train['event_weight']

#Make DFs for each purpose, combining signal and bg
TrainingFrames = [S700Train,S800Train,WTrain,ZTrain]
Training_DF = pd.concat(TrainingFrames, ignore_index=True)

Val700Frames = [S700Val,WVal,ZVal]
Val700_DF = pd.concat(Val700Frames, ignore_index=True)

Test700Frames = [S700Test,WTest,ZTest]
Test700_DF = pd.concat(Test700Frames, ignore_index=True)

Val800Frames = [S800Val,WVal,ZVal]
Val800_DF = pd.concat(Val800Frames, ignore_index=True)

Test800Frames = [S800Test,WTest,ZTest]
Test800_DF = pd.concat(Test800Frames, ignore_index=True)


#Randomize again so signal and BG are mixed.
Training_DF = Training_DF.reindex(
    np.random.permutation(Training_DF.index))

Val700_DF = Val700_DF.reindex(
    np.random.permutation(Val700_DF.index))
Test700_DF = Test700_DF.reindex(
    np.random.permutation(Test700_DF.index))
Val800_DF = Val800_DF.reindex(
    np.random.permutation(Val800_DF.index))
Test800_DF = Test800_DF.reindex(
    np.random.permutation(Test800_DF.index))


#Define scaling functions
def linear_scale(series):
    min_val = series.min()
    max_val = series.max()
    scale = (max_val - min_val) / 2.0
    return series.apply(lambda x:((x - min_val) / scale) - 1.0)
def log_normalize(series):
    return series.apply(lambda x:math.log(x+1.0))

def clip(series, clip_to_min, clip_to_max):
    return series.apply(lambda x:(min(max(x, clip_to_min), clip_to_max)))

def z_score_normalize(series):
    mean = series.mean()
    std_dv = series.std()
    return series.apply(lambda x:(x - mean) / std_dv)



#Define function that normalizes by using functions above. What type and values are found by experimentation
def normalize(examples_dataframe):

    processed_features = pd.DataFrame()
    processed_features["jet_n"] = linear_scale(clip(examples_dataframe["jet_n"],0,10))
    processed_features["jet1_pt"] = log_normalize(examples_dataframe["jet1_pt"])
    processed_features["jet2_pt"] = linear_scale(clip(examples_dataframe["jet2_pt"],0,400))
    processed_features["jet3_pt"] = linear_scale(clip(examples_dataframe["jet3_pt"],0,200))
    processed_features["met"] = log_normalize(clip(examples_dataframe["met"],0,750))
    processed_features["met_phi"] = linear_scale(examples_dataframe["met_phi"])
    processed_features["HT"] = log_normalize(examples_dataframe["HT"])
    processed_features["HT_Sig"] = log_normalize(clip(examples_dataframe["HT_Sig"],0,30))
    processed_features["Meff"] = log_normalize(clip(examples_dataframe["Meff"],0,2000))
    processed_features["DPhi1"] = linear_scale(clip(examples_dataframe["DPhi1"],1,np.pi))                                              
    processed_features["DPhi2"] = linear_scale(examples_dataframe["DPhi2"])
    processed_features["Is_Signal"] = examples_dataframe["Is_Signal"]
    processed_features['event_weight'] = examples_dataframe['event_weight']
    return processed_features

#Normalize every DF
Training_DF = normalize(Training_DF)
Val700_DF = normalize(Val700_DF)
Test700_DF = normalize(Test700_DF)
Val800_DF = normalize(Val800_DF)
Test800_DF = normalize(Test800_DF)

#Save dataframes, both pickled and hdf5
Training_DF.to_pickle('Training_DF.pkl')
Training_DF.to_hdf('Training.h5',key='df')
Val700_DF.to_pickle('Val700_DF.pkl')
Val700_DF.to_hdf('Val700_DF.h5',key='df')
Test700_DF.to_pickle('Test700_DF.pkl')
Test700_DF.to_hdf('Test700_DF.h5',key='df')
Val800_DF.to_pickle('Val800_DF.pkl')
Val800_DF.to_hdf('Val800_DF.h5',key='df')
Test800_DF.to_pickle('Test800_DF.pkl')
Test800_DF.to_hdf('Test800_DF.h5',key='df')


print('Done after (s): ',time.time()-start_time)