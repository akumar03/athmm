#!/usr/bin/perl 
package Beta;

sub new {
  my $class = shift;
  my $self = {
   _strand1 => shift,
   _strand2 => shift,
   _length => shift,
   _parellel => shift,
   _conformation => shift,
  };
  bless $self,$class;
  return $self;
}

sub getStrand1 {
    my( $self ) = @_;
    return $self->{_strand1};
}
sub getStrand2 {
    my( $self ) = @_;
    return $self->{_strand2};
}
sub getLength {
    my( $self ) = @_;
    return $self->{_length};
}
sub getParellel {
    my( $self ) = @_;
    return $self->{_parellel};
}
sub getConformation {
    my( $self ) = @_;
    return $self->{_conformation};
}

1;
