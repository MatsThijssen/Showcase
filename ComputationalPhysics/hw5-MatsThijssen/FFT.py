
# coding: utf-8

# In[191]:


import numpy as np
def DFT(x):
    x=np.asarray(x)
    N=x.shape[0]
    n=np.arange(N)
    k=n.reshape(N,1)
    M=np.exp(-2j*np.pi*k*n/N)
    return(np.dot(M,x))

def iDFT(x):
    x=np.asarray(x)
    N=x.shape[0]
    n=np.arange(N)
    k=n.reshape(N,1)
    M=np.exp(2j*np.pi*k*n/N)
    return((1/N)*np.dot(M,x))

def FFT(x):
    x=np.asarray(x)
    N=int(x.shape[0])
    if N==1:
        return x
    else:
        x_even=FFT(x[::2])
        x_odd=FFT(x[1::2])
        factor = np.exp(-2j * np.pi * np.arange(N) / N)
        return np.concatenate([x_even + factor[:N / 2] * x_odd,
                               x_even + factor[N / 2:] * x_odd])
    
def iFFTs(x):
    x=np.asarray(x)
    N=int(x.shape[0])
    if N==1:
        return x
    else:
        x_even=iFFTs(x[::2])
        x_odd=iFFTs(x[1::2])
        factor = np.exp(2j * np.pi * np.arange(N) / N)
        
        return np.concatenate([x_even + factor[:N / 2] * x_odd,
                                x_even + factor[N / 2:] * x_odd])
def iFFT(x):
    x=np.asarray(x)
    N=x.shape[0]
    result=iFFTs(x)
    return result/N


# In[192]:



#x=np.random.random(1024)
#%timeit DFT(x)
#%timeit FFT(x)
#%timeit np.fft.fft(x)


# In[193]:




