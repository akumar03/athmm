# Program : do_hmmbuild_all.pl
# Developed by : Anoop Kumar
# Date : 04/22/09
# Description : A program that  builds hmms for 0, 20%, NE and NEX mutation alignment files

$prog_hmm = "/r/bcb/anoop/soft/hmmer-2.4i/src/hmmbuild --effent -eloss 0.7 ";


$exp_file = "astralStats.txt";
print "reading exp file $exp_file";

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while($line = <INPUT>){
 print "Reading line $line";
 @words = split('\t',$line);
 $exp_class = $words[0];
  $com1 =  $prog_hmm.$exp_class."_0.hmm ".$exp_class."_0.aln";
  print "Executing:  $com1\n";
  print `$com1`;
  $com1 =  $prog_hmm.$exp_class."_20.hmm ".$exp_class."_20.aln";
  print "Executing:  $com1\n";
  print `$com1`;
  $com1 =  $prog_hmm.$exp_class."_20NE.hmm ".$exp_class."_20NE.aln";
  print "Executing:  $com1\n";
  print `$com1`;
  $com1 =  $prog_hmm.$exp_class."_20NEX.hmm ".$exp_class."_20NEX.aln";
  print "Executing:  $com1\n";
  print `$com1`;
}

