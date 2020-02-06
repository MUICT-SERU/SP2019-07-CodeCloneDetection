#f = open("SelectedClones.csv", "r")
#print (f.read())
# read line by line
# for x in f:
    #print(x)
import os
import shutil

d = []
se = []
s = []
All = [line.rstrip('\n').split(",") for line in open("SelectedClones.csv")]
All1 = [line.rstrip('\n').split(",") for line in open("SelectedFalseClones.csv")]
for i in range(len(All)):
    if All[i][3]  == "default":
        d.append(All[i][0])
    if All[i][3] == "selected":
        se.append(All[i][0])
    if All[i][3] == "sample":
        s.append(All[i][0])
    if All[i][7] == "default":
        d.append(All[i][4])
    if All[i][7] == "selected":
        se.append(All[i][4])
    if All[i][7] == "sample":
        s.append(All[i][4])

for i in range(len(All1)):
    if All1[i][3]  == "default":
        d.append(All1[i][0])
    if All1[i][3] == "selected":
        se.append(All1[i][0])
    if All1[i][3] == "sample":
        s.append(All1[i][0])
    if All1[i][7] == "default":
        d.append(All1[i][4])
    if All1[i][7] == "selected":
        se.append(All1[i][4])
    if All1[i][7] == "sample":
        s.append(All1[i][4])

s_source = '/Users/fern/Desktop/era/4/sample/'
d_source = '/Users/fern/Desktop/era/4/default/'
se_source = '/Users/fern/Desktop/era/4/selected/'

for i in range(len(s)):
    shutil.copyfile(s_source+s[i], '/Users/fern/Desktop/SelectFile/SelectedFiles/sample/'+s[i])

for i in range(len(d)):
    shutil.copyfile(d_source+d[i], '/Users/fern/Desktop/SelectFile/SelectedFiles/default/'+d[i])

for i in range(len(se)):
    shutil.copyfile(se_source+se[i], '/Users/fern/Desktop/SelectFile/SelectedFiles/selected/'+se[i])



