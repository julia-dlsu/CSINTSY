:- dynamic is_a/2.
:- dynamic grandfather_of/2.
:- dynamic father_of/2.
:- dynamic mother_of/2.
:- dynamic sibling_of/2.
:- dynamic parent_of/2.
:- dynamic son_of/2.
:- dynamic daughter_of/2.
:- dynamic son/2.
:- dynamic daughter/2.
:- dynamic child/2.
:- dynamic parent/2.
:- dynamic father/2.
:- dynamic mother/2.
:- dynamic sibling/2.
:- dynamic grandparent/2.
:- dynamic grandfather/2.
:- dynamic grandmother/2.
:- dynamic add_parent/2.
:- dynamic add_sibling/2.
:- dynamic add_grandfather/2.
:- dynamic add_child/2.
:- dynamic aunt/2.
:- dynamic uncle/2.
:- dynamic aunt_of/2.
:- dynamic uncle_of/2.
:- dynamic add_father/2.
:- dynamic add_mother/2.

% A is either a male or female but not both
not(is_a(A, male)) :- is_a(A,female).
not(is_a(A, female)) :- is_a(A,male).

is_relative(A,B) :-
    %scenario: sibling of your parents
    %scenario: children of the sibling of your parents
    (parent_of(A,B), !);
    (parent_of(B,A), !);
    (grandparent_of(A,B), !);
    (grandparent_of(B,A), !);
    (child_of(A,B), !);
    (child_of(B,A), !);
    (sibling_of(A,B), !).

%A is the child of B if they are a child or he/she is a son or daughter of B.
child_of(A,B) :-
    (child(A,B); son(A,B); daughter(A,B), A\==B).

%A is the parent of B if they are a child or he/she is a son or daughter of A.
parent_of(A,B) :-
    ((child(B,A); son(B,A); daughter(B,A), A\==B),!);
    parent(A,B), A\==B.

mother_of(A,B):-
    A\==B,
    \+ is_a(A,male),
    child_of(B,A);
    (sibling(C,B), father(D,C), father(E,B), mother(A,C)).

father_of(A,B):-
    A\==B,
    \+ is_a(A,female),
    child_of(B,A);
    (sibling(C,B), mother(D,C), mother(E,B), father(A,C)).


grandparent_of(A,B) :-
    grandparent(A,B);
    (parent(C,B), parent(A,C));  %fix multiple grandparents
    (uncle(C,B),parent(A,C));
    (aunt(C,B),parent(A,C)).

% A is a sibling of B if (father/mother of A) and (father/mother of C)
% is the same
sibling_of(A,B) :-
    (parent(C,B), parent(C,A), A\==B);
     sibling(A,B);
     sibling(B,A);
    (parent(C,A), grandparent(C,D), child(D,B));
    (parent(C,B), grandparent(C,D), child(D,A));
    (uncle(A,C), parent(B,C));
    (uncle(B,C), parent(A,C));
    (aunt(A,C), parent(B,C));
    (aunt(B,C), parent(A,C)).


% A is a brother/sister of B

brother_of(A,B) :-  is_a(A,male), sibling_of(A,B).
sister_of(A,B) :- is_a(A,female), sibling_of(A,B).


% A is a daughter/son of B

daughter_of(A,B) :- is_a(A,female), child(A,B).
son_of(A,B) :- is_a(A,male), child(A,B).

% A is the grandfather/grandmother of B
grandfather_of(A,B) :-
    \+ is_a(A,female),
    grandfather(A,B);
    (parent(C,B), parent(A,C));  %fix multiple grandparents
    (uncle(C,B),parent(A,C));
    (aunt(C,B),parent(A,C)).

grandmother_of(A,B) :-
    \+ is_a(A,male),
    grandmother(A,B);
    (parent(C,B), parent(A,C));  %fix multiple grandparents
    (uncle(C,B),parent(A,C));
    (aunt(C,B),parent(A,C)).

% A is an aunt/uncle of B
aunt_of(A,B) :-
     A\==B,
     \+ is_a(A,male),
     aunt(A,B);
     (sibling(A,C), child(B,C));
     (sibling(A,C), uncle(C,B));
     (parent(C,A), grandparent(C,B)).


uncle_of(A,B) :-
    A\==B,
    \+ is_a(A,female),
    uncle(A,B);
    (sibling_of(A,C), child_of(B,C));
    (sibling(A,C), aunt(C,B));
    (parent(C,A), grandparent(C,B)).

%A is the child of B
add_child(A,B) :-
    A\==B,
    \+ parent_of(A,B), \+ parent_of(B,A),
    \+ grandparent_of(A,B), \+ grandparent_of(B,A),
    \+ sibling_of(A,B),
    \+ aunt_of(B,A),
    \+ uncle_of(B,A),
    \+ (child_of(C,B), grandparent_of(A,C)),
    %limitation: great great great grandchild
    \+ (grandparent_of(A,C), parent(C,B)),
    \+ (grandparent_of(A,C), grandparent_of(C,B)),
    (parent(B,A) ; assertz(parent(B,A))),
    (child(A,B) ; assertz(child(A,B))),
    ((is_a(A, male), assertz(son(A,B)),!) ; (is_a(A, female), assertz(daughter(A,B)),!) ; true).


%A is the son/daughter of B

add_son(A,B) :-
    A\==B,
    \+ is_a(A, female),
    \+ parent_of(A,B),
    \+ sibling_of(A,B),
    \+ aunt_of(B,A),
    \+ uncle_of(B,A),
    \+ (child_of(C,B), grandparent_of(A,C)),
    \+ (grandparent_of(A,C), parent_of(C,B)),
    \+ (grandparent_of(A,C), grandparent_of(C,B)),
    (parent(B,A) ; assertz(parent(B,A))),
    (son(A,B) ; assertz(son(A,B))),
    (child(A,B) ; assertz(child(A,B))),
    (is_a(A,male) ; assertz(is_a(A,male)), !).

add_daughter(A,B) :-
    A\==B,
    \+ is_a(A, male),
    \+ parent_of(A,B),
    \+ sibling_of(A,B),
    \+ aunt_of(B,A),
    \+ uncle_of(B,A),
    \+ (child_of(C,B), grandparent_of(A,C)),
    \+ (grandparent_of(A,C), parent_of(C,B)),
    \+ (grandparent_of(A,C), grandparent_of(C,B)),
    (parent(B,A) ; assertz(parent(B,A))),
    (daughter(A,B) ; assertz(daughter(A,B))),
    (child(A,B) ; assertz(child(A,B))),
    (is_a(A,female) ; assertz(is_a(A,female)), !).

%A is the parent of B

add_parent(A,B):-
     A\==B,
    \+ parent_of(B,A),
    \+ (father(_,B), mother(_,B)),
    \+ sibling_of(A,B),
    \+ grandparent_of(A,B), \+grandparent_of(B,A),
    \+ (grandparent_of(C,B), parent_of(A,C)),
    \+ (child_of(C,A), grandparent_of(B,C)),

    (((sibling(B,C), parent(Y,C), parent(X,C), parent(Z,B), Y\==X, Y\==Z, X\==Z, A==Y; A==X),
    (parent(A,B); assertz(parent(A, B))),!) ;

    (sibling(_,C); parent(A,B) ; (assertz(parent(A,B)),!))),

    (child(B,A) ; (assertz(child(B,A)),!)),
    ((is_a(B, male), assertz(son(B, A))) ; (is_a(B, female), assertz(daughter(B, A))) ; true).


%A is the mother/father of B

add_father(A,B) :-
    A\==B,
    \+ is_a(A,female),
    \+ father(_,B),
    \+ parent_of(B,A),
    \+ sibling_of(A,B),
    \+ grandparent_of(B,A),\+ grandparent_of(A,B),
    \+ (grandparent_of(C,B), parent_of(A,C)),
    \+ (child_of(C,A), grandparent_of(B,C)),
    (father(A,B); assertz(father(A,B))),
    (parent(A,B) ; assertz(parent(A,B))),
    (child(B, A) ; assertz(child(B,A))),
    (is_a(A, male); assertz(is_a(A,male))),
    ((is_a(B, male), assertz(son(B, A))) ; (is_a(B, female), assertz(daughter(B, A))) ; true);
    ((sibling(B,C), mother(D,C), mother(E,B), father(A,C)), assertz(father(A,B)));
    ((sibling(B,C), mother(D,C), mother(E,B)), (assertz(father(A,B), assertz(father(A,C))))).

add_mother(A,B) :-
    A\==B,
    \+ is_a(A,male),
    \+ mother(_,B),
    \+ parent_of(B,A),
    \+ sibling_of(A,B),
    \+ aunt_of(A,B),
    \+ grandparent_of(B,A),
    \+ grandparent_of(A,B),
    \+ (grandparent_of(C,B), parent_of(A,C)),
    \+ (child_of(C,A), grandparent_of(B,C)),
    (mother(A,B); assertz(mother(A,B))),
    (parent(A,B) ; assertz(parent(A,B))),
    (child(B,A) ; assertz(child(B,A))),
    (is_a(A,female); assertz(is_a(A,female))),
    ((is_a(B, male), assertz(son(B, A))) ; (is_a(B, female), assertz(daughter(B, A))) ; true);
    ((sibling(B,C), father(D,C), father(E,B), mother(A,C)), assertz(mother(A,B)));
    ((sibling(B,C), father(D,C), father(E,B)), (assertz(mother(A,B), assertz(mother(A,C))))).

%A is the grandfather/grandmother of B
add_grandfather(A,B) :-
    A\==B,
    \+ is_a(A, female),
    \+ parent_of(B,A),
    \+ parent_of(A,B),
    \+ grandparent_of(A,B),
    \+ grandparent_of(B,A),
    \+ child_of(B,A),
    \+ child_of(A,B),
    \+ sibling_of(A,B),
    \+ (grandparent_of(C,A), parent_of(C,B)), %gender neutral uncle/aunt
    \+ (parent_of(C,A), grandparent_of(C,B)), %checking if A is an uncle/aunt of B
    (is_a(A, male); assertz(is_a(A,male))),
    (grandfather(A,B); assertz(grandfather(A,B))),
    (grandparent(A,B); assertz(grandparent(A,B))).

add_grandmother(A,B) :-
    A\==B,
    \+ is_a(A, male),
    \+ parent_of(B,A),
    \+ parent_of(A,B),
    \+ grandparent_of(A,B),
    \+ grandparent_of(B,A),
    \+ child_of(B,A),
    \+ child_of(A,B),
    \+ sibling_of(A,B),
    \+ (grandparent_of(C,A), parent_of(C,B)), %gender neutral uncle/aunt
    \+ (parent_of(C,A), grandparent_of(C,B)), %checking if A is an uncle/aunt of B
    (is_a(A, female); assertz(is_a(A,female))),
    (grandmother(A,B); assertz(grandmother(A,B))),
    (grandparent(A,B); assertz(grandparent(A,B))).

%A is the aunt/uncle of B
add_aunt(A,B) :-
    A\==B,
    \+ is_a(A,male),
    \+ parent(A,B),
    \+ parent(B,A),
    \+ grandparent_of(A,B),
    \+ grandparent_of(B,A),
    \+ child_of(B,A),
    \+ child_of(A,B),
    \+ sibling_of(A,B),
    (aunt(A,B); assertz(aunt(A,B))),
    (is_a(A,female); assertz(is_a(A,female))),
    (sister_of(A,C), child_of(D,C), sibling_of(B,D); assertz(aunt(A,D))), %Inheritance to sibling
    (aunt(A,B); assertz(aunt(A,B))).

add_uncle(A,B) :-
     A\==B,
    \+ is_a(A,female),
    \+ parent(A,B),
    \+ parent(B,A),
    \+ grandparent_of(A,B),
    \+ grandparent_of(B,A),
    \+ child_of(B,A),
    \+ child_of(A,B),
    \+ sibling_of(A,B),
    (uncle(A,B); assertz(uncle(A,B))),
    (is_a(A,male); assertz(is_a(A,male))),
    (brother_of(A,C), child_of(D,C), sibling_of(B,D); assertz(uncle(A,D))), %Inheritance to sibling
    (uncle(A,B); assertz(uncle(A,B))).

%A is the sibling of B (gender can be obscure)
add_sibling(A,B) :-
    A\==B,
    (parent(C,A), parent(C,B), assertz(sibling(A,B)));
    (parent(C,A), grandparent(C,B));
    \+ parent_of(B,A),
    \+ parent_of(A,B),
    \+ grandparent_of(A,B),
    \+ grandparent_of(B,A),
    \+ child_of(B,A),
    \+ child_of(A,B),
    \+ aunt_of(A,B),
    \+ aunt_of(B,A),
    \+ uncle_of(A,B),
    \+ uncle_of(B,A),
    (sibling(A,B); assertz(sibling(A,B))).














