import re
from pyswip import Prolog
prolog = Prolog()
prolog.consult("logic.pl")

statements = {
    r'(\w+) and (\w+) are siblings\.$',
    r'(\w+) is a sister of (\w+)\.$',
    r'(\w+) is the mother of (\w+)\.$'
}

def parse_input(input):
    for pattern in statements:
        match = re.match(pattern, input)
        if match:
            groups = match.groups()
            print(f"Statement: {groups}")
            return
    print("Input not recognized.")

def check_query(x):
    if list(x):
        q_result = True
    else:
        q_result = False
    print(q_result, "\n")

# soln = prolog.query("add_child(aa, xx)")
# check_query(soln)

# soln = prolog.query("child(xx, aa)")
# check_query(soln)

# soln = prolog.query("child(aa, xx)")
# check_query(soln)

chat_input = input("Enter a statement: ")
parse_input(chat_input)