Тестовете на проекта са реализирани чрез тестовите бази данни от fromework-а WEKA. Тестовите файлове
могат да се намерят в текущата директория а пускането на реализирания алгоритъм върху някоя от тях
става чрез аргумент при изпълнение на програмата (-t). Например: 
java su.cs08.classifires.functions.NeuralNetwork.class -t "C:\Program Files\Weka-3-4\data\contact-lenses.arff".
или направо от IDE-то.

Ето и резултатите при изпълнение на тестовете:

1. weather.arff

Node 0
    Inputs    Weights
    Threshold    -2.952781824446004
    Node 2    5.045665708033072
    Node 3    2.2709790648055783
    Node 4    2.479413580394074
    Node 5    2.395010753998772
Node 1
    Inputs    Weights
    Threshold    2.9512953894900464
    Node 2    -5.04279687001233
    Node 3    -2.2236860403697882
    Node 4    -2.4559923298615858
    Node 5    -2.462906959466147
Node 2
    Inputs    Weights
    Threshold    -1.3120974541868422
    Attrib outlook=sunny    1.1733306219462063
    Attrib outlook=overcast    2.41019802615419
    Attrib outlook=rainy    -2.3044937695565078
    Attrib temperature    -0.8872815317068973
    Attrib humidity    -3.8210410360826046
    Attrib windy    -0.7743263009408802
Node 3
    Inputs    Weights
    Threshold    -0.7300386205076301
    Attrib outlook=sunny    -1.788192795151081
    Attrib outlook=overcast    2.2861395910874895
    Attrib outlook=rainy    0.16518651825988187
    Attrib temperature    -0.8411475970179373
    Attrib humidity    -0.8334942851967965
    Attrib windy    2.9072378463677313
Node 4
    Inputs    Weights
    Threshold    -0.7419561887385835
    Attrib outlook=sunny    -1.906565325213825
    Attrib outlook=overcast    2.3962065197614457
    Attrib outlook=rainy    0.1841183020264941
    Attrib temperature    -0.8790190413762281
    Attrib humidity    -0.8441096008982062
    Attrib windy    3.0891322482897277
Node 5
    Inputs    Weights
    Threshold    -0.7183542167164157
    Attrib outlook=sunny    -1.8721612994438015
    Attrib outlook=overcast    2.4149529022892198
    Attrib outlook=rainy    0.2233283725926957
    Attrib temperature    -0.8532192210320576
    Attrib humidity    -0.7974871818392061
    Attrib windy    3.0417497832474054
Class yes
    Input
    Node 0
Class no
    Input
    Node 1


Time taken to build model: 0.2 seconds
Time taken to test model on training data: 0 seconds

=== Error on training data ===

Correctly Classified Instances          14              100      %
Incorrectly Classified Instances         0                0      %
Kappa statistic                          1     
Mean absolute error                      0.049 
Root mean squared error                  0.0622
Relative absolute error                 10.5439 %
Root relative squared error             12.981  %
Total Number of Instances               14     


=== Confusion Matrix ===

 a b   <-- classified as
 9 0 | a = yes
 0 5 | b = no



=== Stratified cross-validation ===

Correctly Classified Instances          11               78.5714 %
Incorrectly Classified Instances         3               21.4286 %
Kappa statistic                          0.5116
Mean absolute error                      0.2721
Root mean squared error                  0.4641
Relative absolute error                 57.1494 %
Root relative squared error             94.0596 %
Total Number of Instances               14     


=== Confusion Matrix ===

 a b   <-- classified as
 8 1 | a = yes
 2 3 | b = no
 
 
 
 
 
 
 
 
 
 
 2. weather.nominal.arff

Node 0
    Inputs    Weights
    Threshold    -4.265856384637371
    Node 2    2.2708794930556953
    Node 3    1.8947923869207253
    Node 4    1.1786818030696309
    Node 5    2.5259182623463614
    Node 6    3.710124366800369
Node 1
    Inputs    Weights
    Threshold    4.269939650037863
    Node 2    -2.242182549832101
    Node 3    -1.8936751060916923
    Node 4    -1.196104244812583
    Node 5    -2.584865728753143
    Node 6    -3.6798196066734246
Node 2
    Inputs    Weights
    Threshold    -0.1605020585069864
    Attrib outlook=sunny    -1.2255721224919798
    Attrib outlook=overcast    1.559616486995987
    Attrib outlook=rainy    -0.3125993133074172
    Attrib temperature=hot    -0.27150838460995885
    Attrib temperature=mild    1.1031258581178573
    Attrib temperature=cool    -0.7176321503361603
    Attrib humidity    2.6083365189139243
    Attrib windy    1.7786825543468083
Node 3
    Inputs    Weights
    Threshold    -0.18689240547547473
    Attrib outlook=sunny    -1.0445605071724
    Attrib outlook=overcast    1.4818729932861932
    Attrib outlook=rainy    -0.33294946443320206
    Attrib temperature=hot    -0.25887007125165135
    Attrib temperature=mild    0.9599005296205052
    Attrib temperature=cool    -0.5572798899693898
    Attrib humidity    2.2597737561139484
    Attrib windy    1.5355529374230923
Node 4
    Inputs    Weights
    Threshold    -0.3348718758400699
    Attrib outlook=sunny    -0.46325454968023294
    Attrib outlook=overcast    1.3049547556925585
    Attrib outlook=rainy    -0.5091945403239947
    Attrib temperature=hot    -0.07261328110015669
    Attrib temperature=mild    0.5902545193144364
    Attrib temperature=cool    -0.1796578035367677
    Attrib humidity    1.7254069036265993
    Attrib windy    0.9283962196723478
Node 5
    Inputs    Weights
    Threshold    -0.07510820232756411
    Attrib outlook=sunny    -1.2971511352301706
    Attrib outlook=overcast    1.7031009671955166
    Attrib outlook=rainy    -0.3094103870863947
    Attrib temperature=hot    -0.2872508509100237
    Attrib temperature=mild    1.2130145270746027
    Attrib temperature=cool    -0.8244154546010495
    Attrib humidity    2.899357866111363
    Attrib windy    1.9242974133519843
Node 6
    Inputs    Weights
    Threshold    0.0332113356526811
    Attrib outlook=sunny    -1.6897647773226285
    Attrib outlook=overcast    2.127835104458661
    Attrib outlook=rainy    -0.3756117788503959
    Attrib temperature=hot    -0.3933462717713589
    Attrib temperature=mild    1.5084680185705448
    Attrib temperature=cool    -1.1853000609359416
    Attrib humidity    3.888878455322079
    Attrib windy    2.576360020145886
Class yes
    Input
    Node 0
Class no
    Input
    Node 1


Time taken to build model: 0.28 seconds
Time taken to test model on training data: 0 seconds

=== Error on training data ===

Correctly Classified Instances          14              100      %
Incorrectly Classified Instances         0                0      %
Kappa statistic                          1     
Mean absolute error                      0.0338
Root mean squared error                  0.0484
Relative absolute error                  7.2738 %
Root relative squared error             10.0905 %
Total Number of Instances               14     


=== Confusion Matrix ===

 a b   <-- classified as
 9 0 | a = yes
 0 5 | b = no



=== Stratified cross-validation ===

Correctly Classified Instances          10               71.4286 %
Incorrectly Classified Instances         4               28.5714 %
Kappa statistic                          0.3778
Mean absolute error                      0.2879
Root mean squared error                  0.5242
Relative absolute error                 60.4681 %
Root relative squared error            106.2459 %
Total Number of Instances               14     


=== Confusion Matrix ===

 a b   <-- classified as
 7 2 | a = yes
 2 3 | b = no












3. contact-lenses.arff

Node 0
    Inputs    Weights
    Threshold    3.151702813324399
    Node 3    -0.9322117203570784
    Node 4    -2.81525775558175
    Node 5    -2.578078536117437
    Node 6    -5.932817003265168
Node 1
    Inputs    Weights
    Threshold    -2.298199152071952
    Node 3    -4.619104077477314
    Node 4    -1.6444055740028733
    Node 5    -3.4651736191183704
    Node 6    5.133366474943934
Node 2
    Inputs    Weights
    Threshold    -3.8742569065289003
    Node 3    4.019628909428141
    Node 4    2.9180386930204483
    Node 5    5.736286644707114
    Node 6    0.8417855806599263
Node 3
    Inputs    Weights
    Threshold    0.09314737538129488
    Attrib age=young    -1.7427727811820461
    Attrib age=pre-presbyopic    0.84498571282037
    Attrib age=presbyopic    0.7777268910788188
    Attrib spectacle-prescrip    2.658127605029788
    Attrib astigmatism    1.8960776182984669
    Attrib tear-prod-rate    -4.343163882282214
Node 4
    Inputs    Weights
    Threshold    0.22805877995709306
    Attrib age=young    -0.9576824671356733
    Attrib age=pre-presbyopic    0.48075766490852545
    Attrib age=presbyopic    0.35830161940137817
    Attrib spectacle-prescrip    1.730184057412192
    Attrib astigmatism    1.7887342211945083
    Attrib tear-prod-rate    -3.7116058332407706
Node 5
    Inputs    Weights
    Threshold    -0.8214835214086075
    Attrib age=young    -0.3881009084129684
    Attrib age=pre-presbyopic    -0.406160632185306
    Attrib age=presbyopic    1.5657502342510936
    Attrib spectacle-prescrip    -2.215728445235771
    Attrib astigmatism    -2.5021047925417284
    Attrib tear-prod-rate    -3.8971427780777756
Node 6
    Inputs    Weights
    Threshold    1.6782951778124835
    Attrib age=young    -0.8847909464170246
    Attrib age=pre-presbyopic    -1.4266336738338847
    Attrib age=presbyopic    0.6083085285608033
    Attrib spectacle-prescrip    -1.3915058162003113
    Attrib astigmatism    4.177793474296316
    Attrib tear-prod-rate    -1.5943875087013193
Class soft
    Input
    Node 0
Class hard
    Input
    Node 1
Class none
    Input
    Node 2


Time taken to build model: 0.41 seconds
Time taken to test model on training data: 0 seconds

=== Error on training data ===

Correctly Classified Instances          24              100      %
Incorrectly Classified Instances         0                0      %
Kappa statistic                          1     
Mean absolute error                      0.0428
Root mean squared error                  0.0582
Relative absolute error                 11.6084 %
Root relative squared error             13.7159 %
Total Number of Instances               24     


=== Confusion Matrix ===

  a  b  c   <-- classified as
  5  0  0 |  a = soft
  0  4  0 |  b = hard
  0  0 15 |  c = none



=== Stratified cross-validation ===

Correctly Classified Instances          17               70.8333 %
Incorrectly Classified Instances         7               29.1667 %
Kappa statistic                          0.4766
Mean absolute error                      0.2061
Root mean squared error                  0.3836
Relative absolute error                 54.5606 %
Root relative squared error             87.8314 %
Total Number of Instances               24     


=== Confusion Matrix ===

  a  b  c   <-- classified as
  4  0  1 |  a = soft
  0  2  2 |  b = hard
  1  3 11 |  c = none





4. iris.arff

Node 0
    Inputs    Weights
    Threshold    -3.3422325978302
    Node 3    -0.8336797868689679
    Node 4    8.51148818536377
    Node 5    -4.153276626013119
Node 1
    Inputs    Weights
    Threshold    0.15986151278878907
    Node 3    4.897953460666674
    Node 4    -9.636584896417885
    Node 5    -7.055566106015189
Node 2
    Inputs    Weights
    Threshold    -0.4179694023917375
    Node 3    -4.969463285525741
    Node 4    -3.4820135361422646
    Node 5    7.7347435822815465
Node 3
    Inputs    Weights
    Threshold    4.0649635209965105
    Attrib sepallength    0.9025070613068039
    Attrib sepalwidth    1.8069872820840345
    Attrib petallength    -5.498401369567778
    Attrib petalwidth    -5.482262260971223
Node 4
    Inputs    Weights
    Threshold    -3.226047284778301
    Attrib sepallength    -1.1071502816963348
    Attrib sepalwidth    3.0788609724575524
    Attrib petallength    -4.044158009991576
    Attrib petalwidth    -3.9834037784996763
Node 5
    Inputs    Weights
    Threshold    -5.740665492432562
    Attrib sepallength    -1.2097110566604334
    Attrib sepalwidth    -2.6749399558995224
    Attrib petallength    7.193423151574149
    Attrib petalwidth    7.038404372780843
Class Iris-setosa
    Input
    Node 0
Class Iris-versicolor
    Input
    Node 1
Class Iris-virginica
    Input
    Node 2


Time taken to build model: 1.58 seconds
Time taken to test model on training data: 0 seconds

=== Error on training data ===

Correctly Classified Instances         148               98.6667 %
Incorrectly Classified Instances         2                1.3333 %
Kappa statistic                          0.98  
Mean absolute error                      0.027 
Root mean squared error                  0.0927
Relative absolute error                  6.0703 %
Root relative squared error             19.6559 %
Total Number of Instances              150     


=== Confusion Matrix ===

  a  b  c   <-- classified as
 50  0  0 |  a = Iris-setosa
  0 49  1 |  b = Iris-versicolor
  0  1 49 |  c = Iris-virginica



=== Stratified cross-validation ===

Correctly Classified Instances         145               96.6667 %
Incorrectly Classified Instances         5                3.3333 %
Kappa statistic                          0.95  
Mean absolute error                      0.0372
Root mean squared error                  0.1384
Relative absolute error                  8.3646 %
Root relative squared error             29.3657 %
Total Number of Instances              150     


=== Confusion Matrix ===

  a  b  c   <-- classified as
 50  0  0 |  a = Iris-setosa
  0 46  4 |  b = Iris-versicolor
  0  1 49 |  c = Iris-virginica




И в трите примера виждаме правилна класификация при training-data и 
сравнително добра класификация при cross-validation.