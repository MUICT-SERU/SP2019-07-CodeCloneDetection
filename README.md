# SP2019-07-Code Clone Detection
A repo for the senior project team DoNotCopy.

### Merry Engine Command Line Arguments

| Command Arg     	| Definition                                                     	| Example                                             	|
|-------------------|-----------------------------------------------------------------|-------------------------------------------------------|
| -input          	| input file path                                                	| -input /Users/User/Documents/GitHub/Example         	|
| -size-filter    	| turn on / turn off size filter. Default is on                  	| -size-filter on / off                               	|
| -size-threshold 	| threshold of size filter between 0.0 - 1.0. Default is 0.8     	| -size-threshold 0.8                                 	|
| -training       	| turn on / turn off training mode. Default is off               	| -training on / off                                  	|
| -c2vPath        	| code2vec path                                                  	| -c2vPath /Users/User/Desktop/MerryStorage/code2vec  	|
| -output         	| output file path                                               	| -output /Users/User/Desktop/MerryStorage/output.csv 	|
| -workingDir     	| workspace path                                                 	| -workingDir /Users/User/Desktop/MerryStorage        	|
| -Syntactic      	| turn on / turn off use of Syntactic metrics. Default is on     	| -Syntactic on / off                                 	|
| -Semantic       	| turn on / turn off use of Semantic metrics. Default is on      	| -Semantic on / off                                  	|
| -model          	| model selection (SMO, Dtree / decisionTree, randomForest, SVM) 	| -model SMO                                          	|

