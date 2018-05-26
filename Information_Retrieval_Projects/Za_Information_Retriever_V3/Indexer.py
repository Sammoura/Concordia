from __future__ import print_function

import glob
import json
import re
import os

from bs4 import BeautifulSoup
import nltk
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
from nltk.tokenize.regexp import RegexpTokenizer
from afinn import Afinn
import fnmatch
import ntpath
import operator
ntpath.basename("a/b/c")

stopWords = set(stopwords.words('english'))  
stemmer = PorterStemmer() 

# creates a file for each block and dump the block index (a dictionary) to that file
def createBlock(blockIndex, blockCtr):
    with open('./blocks/block_%s.json' %str(blockCtr).zfill(3), 'w') as blockFile:
        json.dump(blockIndex, blockFile)


# applying SPIMI to each block to create an inverted index for each block
def blockIndexer(docBody, key, blockIndex, removePunct, caseFold, filterNumbers , filterStopWords, stemOk):
    afinn =  Afinn()
    docAfinnScore = 0
    
    if removePunct:
        toker = RegexpTokenizer(r'((?<=[^\w\s])\w(?=[^\w\s])|(\W))+', gaps=True)
        docBodyTokenized = toker.tokenize(docBody)          # tokenizes but remove punctuation 
    else:
        docBodyTokenized = nltk.word_tokenize(docBody)      # tokenization: divides strings into lists of substrings (words and punctuation).
      
    docBodyTokensCount = len(docBodyTokenized)
    
    for token in docBodyTokenized:
        if caseFold:                                        # case folding
            token = token.lower()
        if filterNumbers:                                   # remove numbers
            token = re.sub("[0-9]", "", token)
            if token == "":      continue  
        if filterStopWords:                                 # remove stop words
            if token.lower() in stopWords:  
                continue 
        if stemOk:                                          # stemming
            token = stemmer.stem(token)
            
        valuePair = [key, 1.0]
        
        if token in blockIndex:                         # if the token already exists as a term in the inverted index
            list_pairs = blockIndex[token]
            for pair in list_pairs:
                if key in pair[0]:                      # if docID is not in the posting list of that term, append it
                    pair[1] = float(pair[1])+1          # update document term frequency
                    continue

        else:
            blockIndex[token] = []                      # insert token as a new term in the inverted index
            blockIndex[token].append(valuePair)
        
        tokenAfinnScore = afinn.score(token)
        docAfinnScore += tokenAfinnScore
    
    return [docBodyTokensCount, docAfinnScore] 

# saves a dictionary on disk
def writeToFile(fileName, docDict):
    #collections.OrderedDict(sorted(docDict.items()))
    with open('./inventory/'+fileName+'.json', 'w') as file:
        json.dump(docDict, file, sort_keys = True)

# reads a dictionary from disk
def readBlock(blockPath):
    with open(blockPath, 'r') as block:
        blockIndex = json.load(block)
    return blockIndex  

# reads block index from disk (one at a time) and merge it in memory to final inverted index, then saves on disk
def MergeBlocksIndices():
    finalIndex = {}
    blockFiles = sorted(glob.glob('./blocks/*.json'))
    for file in blockFiles:
        blockIndex = readBlock(file)
        for token in blockIndex:
            if token in finalIndex:
                finalIndex[token] += blockIndex[token]
            else:
                finalIndex[token] = blockIndex[token]
                
    with open('./index/index.json', 'w') as file:
        json.dump(finalIndex, file, sort_keys=True)
    return finalIndex

def calcSent(key, docBody, SentScores):
    afinn =  Afinn()
    docScore = afinn.score(docBody)
    SentScores[key] = docScore
    return docScore

def get_immediate_subdirectories(mainDir):
    return [name for name in os.listdir(mainDir) if os.path.isdir(os.path.join(mainDir, name))]

def get_all_html_of_dir(mainDir):
    return [os.path.join(dirpath, f) for dirpath, dirnames, files in os.walk(mainDir) for f in fnmatch.filter(files, '*.html')]

# Reference: http://stackoverflow.com/questions/8384737/python-extract-file-name-from-path-no-matter-what-the-os-path-format
def path_leaf(path):
    head, tail = ntpath.split(path)
    return os.path.splitext(tail)[0]  or os.path.splitext(ntpath.basename(head))[0]

# creates final inverted index with term frequency    
def createInvertedIndex(blockSize = 1000 , removePunct = False, caseFold = False, filterNumbers = False, filterStopWords = False, stemOk = False): 
       
    blockIndex, docsBodies, docsTitles, SentScores, depSentScores, depSentScoresAvg, docTokenCountDic = {}, {}, {}, {}, {}, {}, {}
    sentScoresToken, depSentScoresToken, depSentScoresTokenAvg = {} , {}, {}
    blockCounter, totalTokens = 0, 0

    # Reference: http://stackoverflow.com/questions/14798220/how-can-i-search-sub-folders-using-glob-glob-module-in-python
    mainDir = 'corpus'
    subDirs = get_immediate_subdirectories(mainDir)            # get department dub directories (no separate files) from main directory
    
    for depName in subDirs:                                    # for each department directory
        depSentScore, depSentScoreToken, docCounter = 0 , 0, 0
        mainFiles = get_all_html_of_dir('./corpus2/'+depName)   # get all files in all sub directories of a department directory

        for pathName in mainFiles:                             # for each file in each department
            docAfinnScoreTokenize = 0;
            docSentScore = 0                                    
            docCounter += 1
            blockCounter+= 1
            document = open(pathName, 'r')                      
            soup = BeautifulSoup(document.read())      
            document.close()
            docBody = soup.get_text()
            docTitle = soup.title.string
#            = depName +'_' + path_leaf(pathName)
            docTitle = depName +'_' + docTitle
            key = str(blockCounter).zfill(3)
            docsBodies[key] = docBody
            docsTitles[key] = docTitle
            [docTokensCount, docAfinnScoreTokenize] = blockIndexer(docBody, key, blockIndex, removePunct, caseFold, filterNumbers , filterStopWords, stemOk)
            docSentScore = calcSent(key, docBody, SentScores)
            docSentScore = docSentScore/docTokensCount
            docAfinnScoreTokenize = docAfinnScoreTokenize/docTokensCount
            SentScores[key] = docSentScore
            sentScoresToken[key] = docAfinnScoreTokenize
            print(key.ljust(5),'    ', docTitle.ljust(50),  '====>', SentScores[key])
            print(key.ljust(5),'    ', docTitle.ljust(50),  '====>', sentScoresToken[key])
            depSentScore += docSentScore
            depSentScoreToken += docAfinnScoreTokenize
            
            docTokenCountDic[key] = docTokensCount
#            print(blockCounter,  '  ', docTokenCountDic[key])
            totalTokens += docTokensCount      
                
            blockDictionary = dict(blockIndex)
            createBlock(blockDictionary, blockCounter)
            blockIndex = {} 
             
        print('\n'.ljust(12), depName, '======> Total number of documents = ', docCounter,'\n'.ljust(12), 'Sentiment Score = ', depSentScore,'    Average Sentiment Score = ',depSentScore/docCounter,'\n') 
        print('\n'.ljust(12), depName, '======> Total number of documents = ', docCounter,'\n'.ljust(12), 'Sentiment Score = ', depSentScoreToken,'    Average Sentiment Score = ',depSentScoreToken/docCounter,'\n')
        print('________________________________________________________________________')
                              
        depSentScores[depName] = depSentScore 
        depSentScoresAvg[depName] = depSentScore/docCounter
        depSentScoresToken[depName] = depSentScoreToken
        depSentScoresTokenAvg[depName] = depSentScoreToken/docCounter
        
    print('Total documents = ', blockCounter)     
    depSentScores = sorted(depSentScores.items(), key = operator.itemgetter(1), reverse = True)
    depSentScoresAvg = sorted(depSentScoresAvg.items(), key = operator.itemgetter(1), reverse = True)    
    depSentScoresToken = sorted(depSentScoresToken.items(), key = operator.itemgetter(1), reverse = True)
    depSentScoresTokenAvg = sorted(depSentScoresTokenAvg.items(), key = operator.itemgetter(1), reverse = True)      

    writeToFile('Doc_Bodies', docsBodies)
    writeToFile('Doc_Titles', docsTitles)
    writeToFile('Doc_Token_Count', docTokenCountDic)
    
    writeToFile('Sentimental_Scores', SentScores)
    writeToFile('Department_Sentimental_Scores', depSentScores)    
    writeToFile('Department_Sentimental_Scores_Avg', depSentScoresAvg)    
    
    writeToFile('Sentimental_Scores_token', sentScoresToken)
    writeToFile('Department_Sentimental_Scores_token', depSentScoresToken)    
    writeToFile('Department_Sentimental_Scores_Avg_token', depSentScoresTokenAvg) 
             
    return [MergeBlocksIndices(), totalTokens]