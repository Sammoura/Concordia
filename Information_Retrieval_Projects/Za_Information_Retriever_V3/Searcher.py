import glob
import json
import os
import time

import nltk
from nltk.compat import raw_input
from nltk.stem.porter import PorterStemmer
from nltk.tokenize.regexp import RegexpTokenizer

import Indexer
import Ranker

alignDist = 50      # FOR FORMATING DITSANCES 

def main():
    invertedIndex, indexInfo, titles , bodies = {}, {} , {}, {}
    indexPath = './index/index.json'
    indexInfoPath = './index/options.json'
    userChoice =''
    userBlockSize, totalTokens, totalTerms, totalPostings = 512, 0, 0, 0
    removePunct, caseFold, filterNumbers, filterStopWords, stemOk = False, False, False, False, False 
       
    if os.path.exists(indexPath):           

        userChoice = input('Enter r to Retrieve the inverted index or c to create it     -----> '.ljust(alignDist))
        while userChoice not in {'r', 'c'}:
            userChoice = input('Enter r to Retrieve the inverted index or c to create it     -----> '.ljust(alignDist))
        if(userChoice is 'r'):
            print('====================================================================')
            print('                Retrieving Inverted Index                ')
            print('====================================================================\n')
            invertedIndex = Indexer.readBlock(indexPath)
            indexInfo = Indexer.readBlock(indexInfoPath)
            removePunct, caseFold, filterNumbers, filterStopWords, stemOk, blockSize, totalTokens, totalTerms, totalPostings = indexInfo['removePunct'], indexInfo['caseFold'], indexInfo['filterNumbers'], indexInfo['filterStopWords'], indexInfo['stemOk'], indexInfo['blockSize'], indexInfo['totalTokens'], indexInfo['totalTerms'], indexInfo['totalPostings']
            
            print('*************** Existing Inverted Index Properties: ****************\n')
            print('Remove Punctuation :'.ljust(alignDist), removePunct)
            print('Case Fold :'.ljust(alignDist), caseFold)
            print('Filter Numbers :'.ljust(alignDist), filterNumbers)
            print('Filter StopWords :'.ljust(alignDist), filterStopWords)
            print('Stemming :'.ljust(alignDist), stemOk)
            print('Memory Block Size :'.ljust(alignDist), blockSize, ' Kb')
            print('Total Number of Tokens :'.ljust(alignDist), totalTokens, ' tokens')
            print('Total Number of dictionary terms:'.ljust(alignDist), totalTerms,' terms') 
            print('Total number of postings:'.ljust(alignDist), totalPostings, ' postings\n') 
            print('\n*********************************************************************\n')
              
    if userChoice is 'c'  or  not os.path.exists(indexPath):
        filelist = glob.glob("./blocks/*.json")                       # remove old blocks
        for f in filelist:
            os.remove(f)
            
        userInput = input('Enter block size in kilo bytes    -----> '.ljust(alignDist))
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
        
        filterinput = input('Remove Punctuation ?     (y/n)    -----> '.ljust(alignDist))
        if(filterinput == 'y'):  
            removePunct = True
        indexInfo['removePunct'] = removePunct
        
        filterinput = input('Case fold ?     (y/n)    -----> '.ljust(alignDist))
        if(filterinput == 'y'):  
            caseFold = True
        indexInfo['caseFold'] = caseFold
        
        filterinput = input('Remove numbers ?     (y/n)    -----> '.ljust(alignDist))
        if(filterinput == 'y'):  
            filterNumbers = True
        indexInfo['filterNumbers'] = filterNumbers
        
        filterinput = input('Remove stop words ?     (y/n)    -----> '.ljust(alignDist))
        if(filterinput == 'y'):  
            filterStopWords = True
        indexInfo['filterStopWords'] = filterStopWords
        
        filterinput = input('Stem using Porter Stemmer ?     (y/n)    -----> '.ljust(alignDist))
        if(filterinput == 'y'):
            stemOk = True
        indexInfo['stemOk'] = stemOk

        print('====================================================================')
        print('                       Creating Inverted Index                      ')
        print('====================================================================')
        startTime = time.time()
        [invertedIndex, totalTokens] = Indexer.createInvertedIndex(blockSize, removePunct, caseFold, filterNumbers, filterStopWords, stemOk)
        endTime = time.time()
        print ('Inverted Index is created in :    ', (endTime - startTime), ' seconds')

        indexInfo['totalTokens'] = totalTokens
        indexInfo['totalTerms'] = len(invertedIndex)
        indexInfo['totalPostings'] = calculatePostings(invertedIndex) 
        
        with open(indexInfoPath, 'w') as optionFile:    # write index information and user options to a file in index directory
            json.dump(indexInfo, optionFile) 
            
    titles = Indexer.readBlock('./inventory/Doc_Titles.json')    
    bodies = Indexer.readBlock('./inventory/Doc_Bodies.json')
    docLenIndex = Indexer.readBlock('./inventory/Doc_Token_Count.json')          # (doc ID --> doc length) index ,retrieves index of documents lengths (number of tokens)
    [l_ave, N]  = getAverageLength(docLenIndex) 
            
    stemmer = PorterStemmer() 
    
    searchChoice = offerSearch()
    while searchChoice == 'y' :
        
        # raw_input([prompt])
        # If the prompt argument is present, it is written to standard output without a trailing newline. 
        # The function then reads a line from input, converts it to a string (stripping a trailing newline), and returns that. 
        # When EOF is read, EOFError is raised
        
        userQuery = raw_input('\nPlease, enter your query term or phrase    -----> '.ljust(alignDist))            # returns a line from user input as a string
        
        # tokenize the query same way done in the inverted index
        if removePunct:
            toker = RegexpTokenizer(r'((?<=[^\w\s])\w(?=[^\w\s])|(\W))+', gaps=True)
            userQueryTokenized = toker.tokenize(userQuery)
        else:
            userQueryTokenized = nltk.word_tokenize(userQuery)
        
        queryFinalResult, freeQueryFinalResult = [], []               # holds intersection set of postings of each query term
        tempResult = []                                               # holds postings lists of each query term (list of posting lists)
        
        for token in userQueryTokenized:
            if stemOk:
                token = stemmer.stem(token)
            if caseFold:
                token = token.lower()
               
            if token in invertedIndex:
                tempResult.append(invertedIndex[token])         # list of lists of pairs [  [ (docID, freq), ...] , [ (docID, freq), ...] ,[ (docID, freq), ...] , ... ]
            else:
                continue
            
        if len(tempResult) == 0:
            print ('Sorry, no matching Document IDs were found corresponding to your query.')
        else:
            [queryFinalResult, freeQueryFinalResult] = Ranker.rankDocsForQuery(tempResult, docLenIndex, N,  l_ave)
            print(queryFinalResult)
            if len(queryFinalResult) == 0:
                print ('Sorry, no matching Document IDs were found corresponding to your query.')
                if(freeQueryFinalResult):
                    searchChoice = input('Do you want a match that may not include all query terms ??    (y/n)    -----> '.ljust(alignDist))
                    if(searchChoice):
                        displayTopN(freeQueryFinalResult, titles, bodies)           
            else:
                displayTopN(queryFinalResult, titles, bodies)

        searchChoice = offerSearch()
    print("Goodbye !!!")

# Offers user to display top N query results
def displayTopN(queryFinalResult, titles, bodies):
    resultCount = len(queryFinalResult)
    print('search returned ', resultCount ,' results')
    
    filterinput = input('Top Number of Documents desired     -----> '.ljust(alignDist))         
    while not filterinput.isdigit() or not int(filterinput) > 0:                                # prompts for a positive integer
        filterinput = input('Top Number of Documents desired     -----> '.ljust(alignDist))
    N = min(int(filterinput), resultCount)
    
    print ('\nMatching documents IDs, Titles and Bodies: ')
    print('\n=====================================<<<    Search Results    >>>======================================')
#    print ('Document ID'.center(20), 'Title'.center(80), sep = '    |    ')
#    print('__________________________________________________________________________________________________________________\n')
    
    for i in range(N):
        elt = queryFinalResult[i][0]
        print (elt.center(20) , titles[elt].center(80), sep = '    |    ')
        print('Document ID:'.ljust(20), elt)
        print('Title:'.ljust(20), titles[elt])
        print('<<< Body >>>'.center(50), bodies[elt], sep = '    \n    ')
        print('________________________________________________________________________________________________________\n')
    print('\n======================================================================================================\n')      

    
def offerSearch():
    searchChoice = input('search     ?    (y/n)    -----> '.ljust(alignDist))
    while searchChoice not in {'y', 'n'}:
        searchChoice = input('search again ?    (y/n)    -----> '.ljust(alignDist))
    return searchChoice

def calculatePostings(index):
    postLen = 0
    for value in index:
        postLen += len(value)
    return postLen

def getAverageLength(docLenIndex):
    l_ave = 0
    N = len(docLenIndex)
    print('Total docs', N)
    totalLength = 0
    for key in docLenIndex:       # sum all doc lengths
        totalLength = totalLength + docLenIndex[key]
        l_ave = float(totalLength/N)
    return [l_ave , N]           # return average

if __name__ == "__main__": main()