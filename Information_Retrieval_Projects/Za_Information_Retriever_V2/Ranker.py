import operator
from math import log10

N = 21578           # NUMBER OF DOCUMENTS IN THE REUTERS CORPUS
k, b = 1.6, 0.75    # OKAPI-BM25 PARAMETERS K [1.2 - 2], B [0.5, 1]

# returns a dictionary of {docID --> doc score of a doc for a term } 
def docScoreOfTerm(termPostingEntry, docLenIndex, l_ave):
    
    term_DocScore = {}                      # dictionary {docID --> doc score of a doc for a term }
    for elt in termPostingEntry:
        docID = elt[0]
        tf = float(elt[1])                 # term frequency     ==> frequency of a term in a document
        df = len(termPostingEntry)         # document frequency ==> number of documents containing the term
        ld = float(docLenIndex[docID])      # document length
        # calculates OKAPI-BM25 RSV formula of a document for a specific term
        denominator = k*( 1 - b +  b*ld/l_ave) + tf 
        docScore = log10(N/df) * (k+1)*tf / denominator
        term_DocScore[docID] = docScore     # A dictionary { docID --> doc Score Of a term}
        
    return term_DocScore

# Combines or merges pairs (docID --> docScore) of every term of a query for AND based query 
def combineScores(query_DocScores, term_DocScore):
    
    copyDict = dict(query_DocScores)        # query_DocScores must be copied because it may change while iterating over it
    for key in copyDict:
        if key in term_DocScore :           # if it is a common doc update old score (add old score to new score)
            query_DocScores[key] = query_DocScores[key] + term_DocScore[key]
        else:                               # Otherwise it is not in the intersection, remove it
            del query_DocScores[key]

# Combines or merges pairs (docID --> docScore) of every term of a query for OR based query 
def freeCombineScores(freeQuery_DocScores, freeTerm_DocScore):
    
    copyDict = dict(freeQuery_DocScores)    # Query_DocScores must be copied because it may change while iterating over it
    for key in copyDict:
        if key in freeTerm_DocScore :       # If it is a common doc update old score (add old score to new score)
            freeQuery_DocScores[key] = freeQuery_DocScores[key] + freeTerm_DocScore[key]
            del freeTerm_DocScore[key]      # Removes not updated entries (already updated and saved in freeQuery_DocScores) to merge the rest after finish looping (difference operation)
    
    freeQuery_DocScores.update(freeTerm_DocScore)   # This adds the terms not in ( freeTerm_DocScore - freeQuery_DocScores)

# Process each posting lists of each query term, and returns a ranked, sorted pairs of (docID, score) using both AND, OR 
def rankDocsForQuery(List_Postings,docLenIndex, l_ave):         
    
    query_DocScores = {}                                                    # Holds scores of documents matches every query term (AND based query)
    freeQuery_DocScores = {}                                                # Holds scores of documents matches some query terms (OR based query)  
    once = True                 
    for termPosting in List_Postings:                                       # Processes one posting list of one query term at a time
        term_DocScore = docScoreOfTerm(termPosting, docLenIndex, l_ave)     # A dictionary of {docID --> doc score of a doc for a term } used by AND based query
        # term_DocScore must be copied because it is passed by reference, so it is mutable
        freeTerm_DocScore = term_DocScore.copy()                            # A dictionary of {docID --> doc score of a doc for a term } used by OR based query
        if (once):                                                          # to initialize previous 2 dictionaries with first return of term_DocScore 
            query_DocScores = dict(term_DocScore)
            freeQuery_DocScores = dict(freeTerm_DocScore)
            once = False
            continue
        if query_DocScores:                                                # If dictionary is not empty (empty means no intersection and it is useless to complete)
            combineScores(query_DocScores, term_DocScore)                  # Merges pairs (docID --> docScore) of every term of a query for AND based query 
        freeCombineScores(freeQuery_DocScores, freeTerm_DocScore)          # Merges pairs (docID --> docScore) of every term of a query for OR based query 
  
    # Sorts in decreasing order of doc scores
    query_DocScores = sorted(query_DocScores.items(), key = operator.itemgetter(1), reverse = True)             
    freeQuery_DocScores = sorted(freeQuery_DocScores.items(), key = operator.itemgetter(1), reverse = True)
    
    return [query_DocScores, freeQuery_DocScores]                          # Returns 2 ranked dictionaries to Searcher for ANd, OR based queries 
    
