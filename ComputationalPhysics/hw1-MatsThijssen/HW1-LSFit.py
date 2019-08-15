
# coding: utf-8

# In[1]:

import numpy as np
import matplotlib.pyplot as plt


# In[10]:

Data=np.loadtxt('millikan.txt')
E_x=0
E_y=0
E_xx=0
E_xy=0
for i in range(len(Data)):
    E_x+=Data[i][0]
    E_y+=Data[i][1]
    E_xx+=(Data[i][0])**2
    E_xy+=Data[i][0]*Data[i][1]
E_x=E_x/len(Data)
E_y=E_y/len(Data)
E_xx=E_xx/len(Data)
E_xy=E_xy/len(Data)

m=(E_xy-E_x*E_y)/(E_xx-E_x**2)
c=(E_xx*E_y-E_x*E_xy)/(E_xx-E_x**2)
print("slope is:", m, "and intercept is:", c)


# In[18]:

Line=np.empty([6,2])
for i in range(len(Data)):
    Line[i][1]=m*Data[i][0]+c
    Line[i][0]=Data[i][0]


# In[26]:




# In[44]:

for i in range(len(Data)):
    plt.plot(Data[i][0],Data[i][1], 'ro')
    plt.plot(Line[i][0],Line[i][1], '-')
plt.show()


# In[46]:




# In[ ]:



