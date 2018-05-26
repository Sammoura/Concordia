from __future__ import print_function

import glob
import json
import re
import sys

from bs4 import BeautifulSoup
import nltk
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
from nltk.tokenize.regexp import RegexpTokenizer

stopWords = set(stopwords.words('english'))  
stemmer = PorterStemmer() 
N = 21578

# creates a file for each block and dump the block index (a dictionary) to that file
def createBlock(blockIndex, blockCtr):
    with open('./blocks/block_%s.json' %str(blockCtr).zfill(5), 'w') as blockFile:
        json.dump(blockIndex, blockFile)


# applying SPIMI to each block to create an inverted index for each block
def blockIndexer(docBody, doc, blockIndex, removePunct, caseFold, filterNumbers , filterStopWords, stemOk):

    if removePunct:
        toker = RegexpTokenizer(r'((?<=[^\w\s])\w(?=[^\w\s])|(\W))+', gaps=True)
        docBodyTokenized = toker.tokenize(docBody)          # tokenizes but remove punctuation 
    else:
        docBodyTokenized = nltk.word_tokenize(docBody)    # tokenization: divides strings into lists of substrings (words and punctuation).
      
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
            
        docID = doc['newid'].zfill(5) 
        valuePair = [docID, 1]
        
        if token in blockIndex:                         # if the token already exists as a term in the inverted index
            list_pairs = blockIndex[token]
            for pair in list_pairs:
                if docID in pair[0]:                    # if docID is not in the posting list of that term, append it
                    pair[1] = float(pair[1])+1          # update document term frequency
                    continue
            blockIndex[token].append(valuePair)
        else:
            blockIndex[token] = []                      # insert token as a new term in the inverted index
            blockIndex[token].append(valuePair)
            
    return docBodyTokensCount 

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

# creates final inverted index with term frequency    
def createInvertedIndex(blockSize = 1000 , removePunct = True, caseFold = False, filterNumbers = False, filterStopWords = False, stemOk = False):    
    docTitles, blockIndex, docTokensCount, docBodies = {}, {}, {}, {}
    blockCounter, docCounter = 0, 0   
    docBodyTokensCount, totalTokens = 0, 0

    # parses the corpse and returns a dictionary of documents
    for pathName in sorted(glob.glob('./corpus/*.sgm')):     # Return a possibly-empty list of path names that match pathname, which must be a string containing a path specification
        document = open(pathName, 'r')                       # open for reading
        yummySoup = BeautifulSoup(document.read())           # Running the document through Beautiful Soup gives us a BeautifulSoup object, which represents the document as a nested (tree) data structure

        document.close()
        documents = yummySoup.findAll('reuters')             # parse the document tree and find all based on <REUTERS>

        for doc in documents:
            docCounter += 1
            key = doc['newid'].zfill(5)
            # if title and body are separate
            if doc.title and doc.body:
                title = doc.title.extract().text
                docBody = str(doc.body.extract().text)
  
            # if title and body are combined
            elif doc.title:
                title = doc.title.extract().text
                docBody = str(title)
            
            # if No body and no title, only text    
            else:
                title = 'NO TITLE'
                docBody = str(doc.find('text').string)
            
            docTitles[key] = title
            docBodies[key] = docBody
            
            docBodyTokensCount = blockIndexer(docBody, doc, blockIndex, removePunct, caseFold, filterNumbers , filterStopWords, stemOk)
            docTokensCount[key] = docBodyTokensCount
            totalTokens += docBodyTokensCount
            
            if sys.getsizeof(blockIndex) >= blockSize*1024 or docCounter == N:
#           if docCounter%blockSize == 0 or docCounter == 21578:
                blockCounter+= 1
                blockDictionary = dict(blockIndex)
                createBlock(blockDictionary, blockCounter)
                blockIndex = {}  
                
    writeToFile('docTitle', docTitles)
    writeToFile('docBody', docBodies)
    writeToFile('docTokenCount', docTokensCount)
    
    return [MergeBlocksIndices(), totalTokens]