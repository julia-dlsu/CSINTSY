from pyswip import Prolog
prolog = Prolog()
prolog.consult("newprolog5.pl")

statements = {
    1: r'(\w+) and (\w+) are siblings\.$',
    2: r'(\w+) is a sister of (\w+)\.$',
    3: r'(\w+) is the mother of (\w+)\.$',
}

def check_query(x):
    if list(x):
        q_result = True
    else:
        q_result = False
    print(q_result, "\n")

soln = prolog.query("add_child(aa, xx)")
check_query(soln)

soln = prolog.query("child(xx, aa)")
check_query(soln)

soln = prolog.query("child(aa, xx)")
check_query(soln)