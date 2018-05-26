#!/usr/bin/env python
# -- coding: utf-8 --
import glob
import json
import os
import time

import nltk
from nltk.compat import raw_input
from nltk.stem.porter import PorterStemmer
from nltk.tokenize.regexp import RegexpTokenizer

import Indexer



def main():
    invertedIndex, titles , indexInfo = {}, {}, {}
    indexPath = './index/index.json'
    indexInfoPath = './index/options.json'
    userChoice =''
    userBlockSize, totalTokens, totalTerms = 512, 0, 0
    removePunct, caseFold, filterNumbers, filterStopWords, stemOk = False, False, False, False, False 
    
    if os.path.exists(indexPath):
        print("Enter r to Retrieve the inverted index or c to create it")
        userChoice = input('-----> ')
        while userChoice not in {'r', 'c'}:
            print("Enter r to Retrieve the inverted index or c to create it")
            userChoice = input('-----> ')
        if(userChoice is 'r'):
            print('_______________________________________________________ ')
            print('                Retrieving Inverted Index                ')
            print('_______________________________________________________\n')
            invertedIndex = Indexer.readBlock(indexPath)
            indexInfo = Indexer.readBlock(indexInfoPath)
            removePunct, caseFold, filterNumbers, filterStopWords, stemOk, blockSize, totalTokens, totalTerms = indexInfo['removePunct'], indexInfo['caseFold'], indexInfo['filterNumbers'], indexInfo['filterStopWords'], indexInfo['stemOk'], indexInfo['blockSize'], indexInfo['totalTokens'], indexInfo['totalTerms']
            
            print('*************** Existing Inverted Index Properties: ****************')
            print('Remove Punctuation :'.ljust(25), removePunct)
            print('Case Fold :'.ljust(25), caseFold)
            print('Filter Numbers :'.ljust(25), filterNumbers)
            print('Filter StopWords :'.ljust(25), filterStopWords)
            print('Stemming :'.ljust(25), stemOk)
            print('Memory Block Size :'.ljust(25), blockSize, ' Kilo bytes')
            print('Total Number of Tokens :'.ljust(25), totalTokens)
            print('Number of dictionary terms:'.ljust(25), totalTerms, '\n') 
                
    if userChoice is 'c'  or  not os.path.exists(indexPath):
        filelist = glob.glob("./blocks/*.json")                       # remove old blocks
        for f in filelist:
            os.remove(f)
            
        print('Enter block size in kilo bytes')
        userInput = input('-----> ')
        try:
            userBlockSize = int(userInput)
        except ValueError:
            print("That's not an integer !")
            
        if userBlockSize > 0:
            blockSize = userBlockSize
#       print(blockSize, "documents per block\n" )
        else:
            blockSize = 512
        indexInfo['blockSize'] = blockSize
        
        print('Remove Punctuation ?     (y/n)')
        filterinput = input('-----> ')
        if(filterinput == 'y'):  
            removePunct = True
        indexInfo['removePunct'] = removePunct
        
        print('Case fold ?     (y/n)')
        filterinput = input('-----> ')
        if(filterinput == 'y'):  
            caseFold = True
        indexInfo['caseFold'] = caseFold
        
        print('Remove numbers ?     (y/n)')
        filterinput = input('-----> ')
        if(filterinput == 'y'):  
            filterNumbers = True
        indexInfo['filterNumbers'] = filterNumbers
        
        print('Remove stop words ?     (y/n)')
        filterinput = input('-----> ')
        if(filterinput == 'y'):  
            filterStopWords = True
        indexInfo['filterStopWords'] = filterStopWords
        
        print('Stem using Porter Stemmer ?     (y/n)')
        filterinput = input('-----> ')
        if(filterinput == 'y'):
            stemOk = True
        indexInfo['stemOk'] = stemOk

        print('_______________________________________________________ ')
        print('                Creating Inverted Index                ')
        print('_______________________________________________________\n')
        startTime = time.time()
        [invertedIndex, totalTokens] = Indexer.createInvertedIndex(blockSize, removePunct, caseFold, filterNumbers, filterStopWords, stemOk)
        endTime = time.time()
        print ('Inverted Index is created in :    ', (endTime - startTime), ' seconds')

        indexInfo['totalTokens'] = totalTokens
        indexInfo['totalTerms'] = len(invertedIndex)
        
        with open(indexInfoPath, 'w') as optionFile:    # write index information and user options to a file in index directory
            json.dump(indexInfo, optionFile) 
               
    stemmer = PorterStemmer() 
    
    searchChoice = offerSearch()
    while searchChoice == 'y' :
        print("\nPlease, enter your query term or phrase")
        
        # raw_input([prompt])
        # If the prompt argument is present, it is written to standard output without a trailing newline. 
        # The function then reads a line from input, converts it to a string (stripping a trailing newline), and returns that. 
        # When EOF is read, EOFError is raised
        
        userQuery = raw_input('-----> ')            # returns a line from user input as a string
        
        # tokenize the query same way done in the inverted index
        if removePunct:
            toker = RegexpTokenizer(r'((?<=[^\w\s])\w(?=[^\w\s])|(\W))+', gaps=True)
            userQueryTokenized = toker.tokenize(userQuery)
        else:
            userQueryTokenized = nltk.word_tokenize(userQuery)
        
        queryFinalResult = []               # holds intersection set of postings of each query term
        tempResult = []        # holds postings of each query term (list of posting lists)
        
        for token in userQueryTokenized:
            if stemOk:
                token = stemmer.stem(token)
            if caseFold:
                token = token.lower()
                
            if token in invertedIndex:
                tempResult.append(invertedIndex[token])
            else:
                continue
        
        if len(tempResult) == 0:
            print ('Sorry, no matching Document IDs were found corresponding to your query.')
        else:
            queryFinalResult = list(set.intersection(*map(set, tempResult)))
        
            if len(queryFinalResult) == 0:
                print ('Sorry, no matching Document IDs were found corresponding to your query.')
            else:
                titlesPath = './titles/titles.json'
                titles = Indexer.readBlock(titlesPath)
                print ('Matching documents IDs and their Titles: ')
                print('\n_________________________________________________________________________________________________________________')
                print ('Document ID'.center(20), 'Title'.center(80), sep = '    |    ')
                print('__________________________________________________________________________________________________________________\n')
                for elt in queryFinalResult:
                    print (elt.center(20) , titles[elt].center(80), sep = '    |    ')
                print('__________________________________________________________________________________________________________________\n')
                
        searchChoice = offerSearch()
    print("Goodbye !!!")

def offerSearch():
    print ('search ?    (y/n)')
    searchChoice = input('----> ')
    while searchChoice not in {'y', 'n'}:
        print ('search again ?    (y/n)')
        searchChoice = input('----> ')
    return searchChoice

if __name__ == "__main__": main() 
