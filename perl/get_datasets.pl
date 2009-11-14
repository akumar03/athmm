# Program : get_datasets.pl
# Developed by : Anoop Kumar
# Date : 11/05/09
# Description : A program that gets all the datasets from moon.cs.tufts.edu

use strict;

my $exp_file = $ARGV[0];
print "reading exp file $exp_file";

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while( my $line = <INPUT>){
# if ($line =~ m/b\.1\.18\.10/) {
 print "Reading line $line\n";
 my @words = split(',',$line);
 my $family_id = $words[0];
 my $family_file = $family_id."_0.fasta";
 print "Reading family file $family_id \n";
 open(FAM,$family_file);
 
 while(my $f_line = <FAM>) {
  if ($f_line =~ m/\>/) {
#       print "line: $f_line";
        my @f_words = split(/\s+/,$f_line);
        my $pdb_id = substr($f_words[0],1);
        my $com = "cp /cluster/shared/akumar03/astral/seqs/$pdb_id.ent pdb";
	print "$com\n";
        print `$com`;
       
  }
 }
 close FAM;
# }
}
close INPUT;

