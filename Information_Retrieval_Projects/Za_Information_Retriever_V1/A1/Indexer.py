#!/usr/bin/env python
# -- coding: utf-8 --
from __future__ import print_function

import glob
import json
import re
import string
import sys

from bs4 import BeautifulSoup
import nltk
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
from nltk.tokenize.regexp import RegexpTokenizer
import codecs

stopWords = set(stopwords.words('english'))  
stemmer = PorterStemmer() 

# Sorts the passed list as humans expect, might be used to sort files by name naturally
def naturalKey(string):
    return [int(s) if s.isdigit() else s for s in re.split(r'(\d+)', string)]

# creates a file for each block and dump the block index (a dictionary) to that file
def createBlock(blockIndex, blockCtr):
    with open('./blocks/block_%s.json' %str(blockCtr).zfill(5), 'w') as blockFile:
        json.dump(blockIndex, blockFile, sort_keys=True)

# applying SPIMI to each block to create an inverted index for each block
def blockIndexer(docBody, doc, blockIndex, removePunct, caseFold, filterNumbers , filterStopWords, stemOk):
#    docBody.translate(string.punctuation)
    if removePunct:
        toker = RegexpTokenizer(r'((?<=[^\w\s])\w(?=[^\w\s])|(\W))+', gaps=True)
        docBodyTokenized = toker.tokenize(docBody)          # tokenizes but remove punctuation 
    else:
        docBodyTokenized = nltk.word_tokenize(docBody)    # tokenization: divides strings into lists of substrings (words and punctuation).
        
    tokensSoFar = len(docBodyTokenized)
    
    for token in docBodyTokenized:
        if caseFold:
            token = token.lower()
        if filterNumbers:
            token = re.sub("[0-9]", "", token)
            if token == "":      continue  
        if filterStopWords:
            if token.lower() in stopWords:  
                continue 
        if stemOk:
            token = stemmer.stem(token)
          
        if token in blockIndex:                      # if the token already exists as a term in the inverted index
            if doc['newid'] in blockIndex[token]:    # if docID is not in the posting list of that term, append it
                continue
            blockIndex[token].append(doc['newid'])
        else:
            blockIndex[token] = []                    # insert token as a new term in the inverted index
            blockIndex[token].append(doc['newid'])
            
    return tokensSoFar

def writeTitles(docTitles):
    with open('./titles/titles.json', 'w') as titlesFile:
        json.dump(docTitles, titlesFile, sort_keys=True)


def readBlock(blockPath):
    with open(blockPath, 'r') as block:
        blockIndex = json.load(block)
    return blockIndex  


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


def filterToken(token, caseFold, filterNumbers , filterStopWords):
    if(caseFold):
        token = token.lower()
        
    if(filterNumbers):
        token = re.sub("[0-9]", "", token)
        
    if(filterStopWords):
        if token in stopwords.words("english"):
            token = "" 

    return token

       
def createInvertedIndex(blockSize = 1000 , removePunct = True, caseFold = False, filterNumbers = False, filterStopWords = False, stemOk = False):    
    docTitles, blockIndex = {}, {}
    blockCounter = 0
    docCounter = 0   
    tokensSoFar = 0 
    totalTokens = 0                           # total number of documents processed so far
    # parses the corpse and returns a dictionary of documents
    for pathName in sorted(glob.glob('./corpus/*.sgm')):     # Return a possibly-empty list of path names that match pathname, which must be a string containing a path specification
        document = open(pathName, 'rb')                       # open for reading in binary
        yummySoup = BeautifulSoup(document.read(), "html.parser") # Running the document through Beautiful Soup gives us a BeautifulSoup object, which represents the document as a nested (tree) data structure

        document.close()
        documents = yummySoup.findAll('reuters')             # parse the document tree and find all based on <REUTERS>

        for doc in documents:
            docCounter += 1
    
            # if title and body are separate
            if doc.title and doc.body:
                docTitles[doc['newid']] = doc.title.extract().text
                docBody = str(doc.body.extract().text)
  
            # if title and body are combined
            elif doc.title:
                docTitles[doc['newid']] = doc.title.extract().text
                docBody = str(docTitles[doc['newid']])
            
            # if No body and no title, only text    
            else:
                docTitles[doc['newid']] = 'NO TITLE'
                docBody = str(doc.find('text').string)

            tokensSoFar = blockIndexer(docBody, doc, blockIndex, removePunct, caseFold, filterNumbers , filterStopWords, stemOk)
            totalTokens += tokensSoFar

            if sys.getsizeof(blockIndex) >= blockSize*1024 or docCounter == 21578:
#           if docCounter%blockSize == 0 or docCounter == 21578:
                blockCounter+= 1
                dictionary = dict(blockIndex)
                createBlock(dictionary, blockCounter)
                blockIndex = {}  
                
    writeTitles(docTitles)
    return [MergeBlocksIndices(), totalTokens]
