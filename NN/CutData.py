#Cuts data loosely to prepare for NN.

import numpy as np
import sys 
import uproot
import pandas as pd
import LorentzVector as LV


#Run over one set at a time, either W,Z, 700, or 800 
if (sys.argv[1] != 'W' and sys.argv[1] != 'Z' and sys.argv[1] != 'Signal700' and sys.argv[1] != 'Signal800'):
    raise Exception('Bad argument')
if (sys.argv[1] == 'Signal700'):
    imported = uproot.open('Signal.root')['SS_700_695_NoSys']
elif (sys.argv[1] == 'Signal800'):
    imported = uproot.open('Signal.root')['SS_800_795_NoSys']
else:
    imported = uproot.open(sys.argv[1]+'jets_split/'+sys.argv[1] + 'jets_' + sys.argv[2]+'.root')['nominal']
    
#Put all variables we want to use here
UsedData = imported.arrays(['jet_n','jet_pt','el_n','mu_n','met','met_phi', 'baselineLep_n','jet_eta','jet_phi','jet_m','event_weight'])


#Function to take care of different size arrays in array (pTs). All will be same length (based on event with most jets), with zeros in end if space left over
def JaggedFixer(array, array_n):
    new_array = np.zeros([array.size, array_n.max()])
    for i in range(len(array)):
        if (array[i].size!=0):
            new_array[i][:array[i].size] = array[i]
    return new_array

#Change this function based on what data we are using
def preprocess(DataDict):
    
    processed = {}
    processed['met_phi'] = DataDict[b'met_phi']
    
    processed['jet_n'] = DataDict[b'jet_n']
    tempPT = JaggedFixer(DataDict[b'jet_pt'],DataDict[b'jet_n'])
    processed['jet1_pt'] = tempPT[:,0] #momenta of 1st jet, 0 if no jets
    processed['jet2_pt'] = tempPT[:,1] #momenta of 2nd jet, 0 if no 2nd jet
    processed['jet3_pt'] = tempPT[:,2] #same
    
    tempEta = JaggedFixer(DataDict[b'jet_eta'],DataDict[b'jet_n'])
    processed['jet1_eta'] = tempEta[:,0] #eta of 1st jet, 0 if no jets
    processed['jet2_eta'] = tempEta[:,1]
    
    tempPhi = JaggedFixer(DataDict[b'jet_phi'],DataDict[b'jet_n'])
    processed['jet1_phi'] = tempPhi[:,0] #phi of 1st jet, 0 if no jets
    processed['jet2_phi'] = tempPhi[:,1]
    
    tempM = JaggedFixer(DataDict[b'jet_m'],DataDict[b'jet_n'])
    processed['jet1_m'] = tempM[:,0] #mass of 1st jet, 0 if no jets
    processed['jet2_m'] = tempM[:,1]
    
    processed['HT'] = DataDict[b'jet_pt'].sum() #Scalar sum of jet_pt's, known has HT
    processed['met'] = DataDict[b'met']
    processed["Is_Signal"] = ((DataDict[b'jet_n']>=0) & ((sys.argv[1] == 'Signal700') or (sys.argv[1] == 'Signal800'))).astype('float') #0 if BG, 1 if signal
    processed['event_weight'] = DataDict[b'event_weight']
    
    return processed

#Variables we're actually dealing with after prep
Variables = ['jet_n','HT','jet1_pt','jet2_pt','jet3_pt','met','met_phi','jet1_eta','jet2_eta','jet1_phi','jet2_phi','jet1_m','jet2_m','Is_Signal','event_weight'] 
ProcessedData = preprocess(UsedData)
#At this point, have a dict with all data-variables as keys and good arrays as values.

#Choose cuts we want to enforce, create array of indices of events that fall outside of cut 
removedIndeces = []
for i in range(len(UsedData[b'mu_n'])):
    if (UsedData[b'mu_n'][i] != 0 or UsedData[b'el_n'][i] != 0 or UsedData[b'baselineLep_n'][i]!=0 or ProcessedData['jet_n'][i]<1  or ProcessedData['met'][i]<110): #or ProcessedData['jet1_pt'][i]>1450 or ProcessedData['jet2_pt'][i]>600):
    	removedIndeces.append(i)

def CutData(ProcessedData, removedIndeces, Variables):
    
    CutData = {}
    for i in Variables:
    	CutData[i] = np.delete(ProcessedData[i],removedIndeces,None)
    return CutData

CutDataDict = CutData(ProcessedData, removedIndeces, Variables)
#Now we have a dictionary with the non-wanted data cut out. But still want HT_sig and other dependencies (that could be zero b4)
#This function adds HT_sig etc. which will now all be non-zero/non-infinite

def DPhi(met, met_phi, jet_pt,jet_eta,jet_phi,jet_m):
    Dphi = np.zeros([met.size])
    MET_TLV = LV.LorentzVector()
    Jet_TLV = LV.LorentzVector()
    for i in range(met.size):
        MET_TLV.SetXYZT(met[i]*np.cos(met_phi[i]),met[i]*np.sin(met_phi[i]),0,0)
        Jet_TLV.SetPtEtaPhiM(jet_pt[i],jet_eta[i],jet_phi[i],jet_m[i])
        DeltaPhi = abs(MET_TLV.DeltaPhi(Jet_TLV))
        Dphi[i] = DeltaPhi
    
    return Dphi

#Do the same thing as before, new dictionary with some new variables added
def postprocess(DataDict):
    processed = {}
    processed['met_phi'] = DataDict['met_phi']
    #processed['baselineLep_n'] = DataDict[b'baselineLep_n']
    
    processed['jet_n'] = DataDict['jet_n']
    
    processed['jet1_pt'] = DataDict['jet1_pt'] #momenta of 1st jet, 0 if no jets
    processed['jet2_pt'] = DataDict['jet2_pt'] #momenta of 2nd jet, 0 if no 2nd jet
    processed['jet3_pt'] = DataDict['jet3_pt'] #same
    
    processed['HT'] = DataDict['HT']
    processed['Meff'] = DataDict['met']+DataDict['HT']
    processed['HT_Sig'] = (DataDict['met']/np.sqrt(DataDict['HT'])) #HT_sig = MET/(HT)^(1/2) 
    #processed['jet_pt'] = JaggedFixer(DataDict[b'jet_pt'],DataDict[b'jet_n'])
    
    processed['DPhi1'] = DPhi(DataDict['met'],DataDict['met_phi'],DataDict['jet1_pt'],DataDict['jet1_eta'],DataDict['jet1_phi'],DataDict['jet1_m'])
    processed['DPhi2'] = DPhi(DataDict['met'],DataDict['met_phi'],DataDict['jet2_pt'],DataDict['jet2_eta'],DataDict['jet2_phi'],DataDict['jet2_m'])
    
    processed['met'] = DataDict['met']
    processed['Is_Signal'] = DataDict['Is_Signal']
    processed['event_weight'] = DataDict['event_weight']
    
    
    
    return processed

#Variables we now have
PostVariables = ['jet_n','jet1_pt','jet2_pt','jet3_pt','met','met_phi','HT','HT_Sig','Meff','DPhi1','DPhi2','Is_Signal','event_weight']

PostData = postprocess(CutDataDict)

#If additional cuts need to be made on variables that couldn't be introduced until now (HT_Sig, Meff, Dphi's), do that here
PostremovedIndeces = []
for i in range(len(PostData['met'])):
    if (PostData['HT_Sig'][i]<6 or PostData['Meff'][i]<300):
    	PostremovedIndeces.append(i)
    	
FinalData = CutData(PostData,PostremovedIndeces, PostVariables)

#print("Number of events:", len(FinalData['met']))

#Save to DF
DataFrame = pd.DataFrame.from_dict(FinalData)
if (len(sys.argv)>2):
    DataFrame.to_pickle(sys.argv[1]+sys.argv[2]+'DF.pkl')
else:
    DataFrame.to_pickle(sys.argv[1]+'DF.pkl')

