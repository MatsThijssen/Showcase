#Small class that defines a 4-vector, akin to ROOT Lorentz Vectors. Used by CutData.py to create DPhi's    

import numpy as np


class LorentzVector:
    
    X = 0
    Y = 0
    Z = 0
    T = 0
    Phi = 0
    
   
        
    
    def SetXYZT(self,x,y,z,t):
        self.X = x
        self.Y = y
        self.Z = z
        self.T = t
        self.Phi = np.arctan2(y,x)
    
    def SetPtEtaPhiM(self,pt,eta,phi,m):
        pt = abs(pt)
        self.SetXYZM(pt*np.cos(phi),pt*np.sin(phi),pt*np.sinh(eta),m)
        
    def SetXYZM(self, x, y, z, m):
        if (m >= 0):
            self.SetXYZT(x, y, z, np.sqrt(x*x+y*y+z*z+m*m) )
        else:
            self.SetXYZT(x, y, z, np.sqrt( max((x*x+y*y+z*z-m*m), 0) ) )
            
    def DeltaPhi(self, LV):
        ang = (self.Phi - LV.Phi)
        return self.Phi_mpi_pi( ang )
        
    def Phi_mpi_pi(self, x ):
        while (x >=np.pi):
            x -=2*np.pi 
        while (x < -np.pi):
            x+=2*np.pi
        return x