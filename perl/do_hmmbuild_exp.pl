# Program : do_hmmbuild_all.pl
# Developed by : Anoop Kumar
# Date : 06/25/09
# Description : A program that  builds hmms for 0, 20%, NE and NEX mutation alignment files

$prog_hmm = "/r/bcb/anoop/soft/hmmer-2.4i/src/hmmbuild --effent --eloss 0.7 ";


$exp_file = "astralStats.txt";
print "reading exp file $exp_file";

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while($line = <INPUT>){
 print "Reading line $line";
 @words = split('\t',$line);
 $exp_class = $words[0];
  $str1 = "_NONE_NONE_NONE_NONE";
  $com1 =  $prog_hmm.$exp_class.$str1.".hmm ".$exp_class.$str1.".aln";
  print "Executing:  $com1\n";
  print `$com1`;
  $str2 = "_NONE_NONE_20_10";
  $com2 =  $prog_hmm.$exp_class.$str2.".hmm ".$exp_class.$str2.".aln";
  print "Executing:  $com2\n";
  print `$com2`;
  $str3 = "_2_X_20_10";
  $com3 =  $prog_hmm.$exp_class.$str3.".hmm ".$exp_class.$str3.".aln";
  print "Executing:  $com3\n";
  print `$com3`;
  $str4 = "_2_EX_NONE_NONE";
  $com4 =  $prog_hmm.$exp_class.$str4.".hmm ".$exp_class.$str4.".aln";
  print "Executing:  $com1\n";
  print `$com4`;
}
