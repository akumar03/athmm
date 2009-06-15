# Program : do_hmmbuild_all.pl
# Developed by : Anoop Kumar
# Date : 04/22/09
# Description : A program that  builds hmms for 0, 20%, NE and NEX mutation alignment files

$prog_hmm = "/r/bcb/anoop/soft/hmmer-2.4i/src/hmmbuild --effent --eloss 0.7 ";


$exp_file = "astralStats.txt";
print "reading exp file $exp_file";

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while($line = <INPUT>){
 print "Reading line $line";
 @words = split('\t',$line);
 $exp_class = $words[0];
  for($i=10;$i<=60;$i += 10) {
  print `$com1`;
  $com1 =  $prog_hmm.$exp_class."_".$i.".hmm ".$exp_class."_".$i.".aln";
  print "Executing:  $com1\n";
  print `$com1`;
  $com1 =  $prog_hmm.$exp_class."_".$i."NE.hmm ".$exp_class."_".$i."NE.aln";
  print "Executing:  $com1\n";
  print `$com1`;
 }
}
