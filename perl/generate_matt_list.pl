# Program : generate_matt_list.pl
# Developed by : Anoop Kumar
# Date : 11/06/09
# Description : A program that gets all the datasets from moon.cs.tufts.edu

use strict;

my $exp_file = $ARGV[0];
print "reading exp file $exp_file";

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while( my $line = <INPUT>){
 print "Reading line $line\n";
 my @words = split(',',$line);
 my $family_id = $words[0];
 my $family_file = $family_id."_0.fasta";
 print "Reading family file $family_id \n";
 open(FAM,$family_file);
 my $pdbs ="";
 while(my $f_line = <FAM>) {
  if ($f_line =~ m/\>/) {
        my @f_words = split(/\s+/,$f_line);
        my $pdb_id = substr($f_words[0],1);
        $pdbs .= "pdb/".$pdb_id."\n";
       
  }
 }
 close FAM;
 open(OUT,">".$family_id.".lst");
 print OUT $pdbs;
 close OUT;
# execute matt
 my $com = "/cluster/tufts/protein/se2/Matt -o ".$family_id."_0_matt -L ".$family_id.".lst";
 print "Executing: $com\n";
 print `$com`;

# execute smurf
 $com = "/cluster/tufts/protein/se2/smurf/SmurfPreparse ".$family_id."_0_matt";
 print "Executing: $com\n";
 print `$com`;
 
# }
}
close INPUT;

