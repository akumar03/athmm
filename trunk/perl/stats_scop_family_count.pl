# Program: stat_scop_family_count.pl
# Developed by: Anoop Kumar
# Date: 3/16/2010
# Description:  This program prints the number of seqeuences in families in the scop superfmailes studied

use strict;
my $exp_file = $ARGV[0];

my $beta_file = "beta.txt";
my %sfamily_count;
my %family_count;
open(BETA,$beta_file) or die("Can't open the  file: $beta_file");
while(my $line = <BETA>) {
if(!($line =~ m/^#/))  {
#  print $line;
 count_family($line);
# print "\n";
}
}
close BETA;

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while(my $exp_line = <INPUT>) {
#  print "$exp_line";
  my @words = split(",",$exp_line);
#  print $words[0]." ".$sfamily_map{get_super_family($words[0])}."\n";
  print $words[0]." ".$family_count{$words[0]}." ".$sfamily_count{get_super_family($words[0])}."\n";
  
}
close INPUT;
close OUT;

sub count_family {
  my $line = shift;
  my @words = split(/\s/,$line);
  my $family = $words[0];
  my @parts = split(/\./,$words[0]);
  if($#parts == 3) {	
    $family_count{$family}= $words[1];
    my $sfamily = get_super_family($family);
    if(exists($sfamily_count{$sfamily})){
	$sfamily_count{$sfamily} += $words[1];

    } else {
	$sfamily_count{$sfamily} = $words[1];
    }  
  }

}

sub get_super_family {
  my $family = shift;
  my @parts = split(/\./,$family);
  return "$parts[0].$parts[1].$parts[2]";

}
