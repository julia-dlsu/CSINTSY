import re
from pyswip import Prolog
prolog = Prolog()
prolog.consult("logic.pl")

statements = {
    1: r'(\w+) and (\w+) are siblings\.$',
    2: r'(\w+) is a sister of (\w+)\.$',
    3: r'(\w+) is the mother of (\w+)\.$',
    4: r'(\w+) is a grandmother of (\w+)\.$',
    5: r'(\w+) is a child of (\w+)\.$',
    6: r'(\w+) is a daughter of (\w+)\.$',
    7: r'(\w+) is an uncle of (\w+)\.$',
    8: r'(\w+) is a brother of (\w+)\.$',
    9: r'(\w+) is the father of (\w+)\.$',
    10: r'(\w+) and (\w+) are the parents of (\w+)\.$',
    11: r'(\w+) is a grandfather of (\w+)\.$',
    12: r'(\w+) is a son of (\w+)\.$',
    13: r'(\w+) is an aunt of (\w+)\.$',
}

def parse_input(input):
    for key, pattern in statements.items():
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