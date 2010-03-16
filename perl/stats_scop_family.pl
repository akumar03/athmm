# Program: stat_scop_family.pl
# Developed by: Anoop Kumar
# Date: 3/16/2010
# Description:  This program prints the number of families in the scop superfmailes studied

use strict;
my $exp_file = $ARGV[0];
my $scop_file = "dir.des.scop_1.73.txt";

my %sfamily_map;
open(SCOP,$scop_file) or die("Can't open the SCOP file: $scop_file");
while(my $line = <SCOP>) {
if(!($line =~ m/^#/))  {
 my @words = split(/\s/,$line);
# print $words[2];
 add_family($words[2]);
# print "\n";
}
}
close SCOP;

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while(my $exp_line = <INPUT>) {
#  print "$exp_line";
  my @words = split(",",$exp_line);
  my @families = split(" ",$sfamily_map{get_super_family($words[0])});
#  print $words[0]." ".$sfamily_map{get_super_family($words[0])}."\n";
  print "".($#families+1)."\n";
}
close INPUT;
close OUT;

sub add_family {
  my $family = shift;
  my @parts = split(/\./,$family);
  if($#parts == 3) {	
    my $sfamily = get_super_family($family);
#    print " ".$sfamily;
    if(exists($sfamily_map{$sfamily})) {
	my $families = $sfamily_map{$sfamily};
        if(!($families =~ m/$family/)){
  	   $families .= " ".$family;   	      
  	   $sfamily_map{$sfamily} = $families;
        } 
    } else{
 	$sfamily_map{$sfamily} = $family;
    }
#    print " ".$sfamily_map{$sfamily};
  }

}

sub get_super_family {
  my $family = shift;
  my @parts = split(/\./,$family);
  return "$parts[0].$parts[1].$parts[2]";

}
