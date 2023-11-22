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
    10: r'(.+?) are children of (\w+)\.$',
    11: r'(\w+) and (\w+) are the parents of (\w+)\.$',
    12: r'(\w+) is a grandfather of (\w+)\.$',
    13: r'(\w+) is a son of (\w+)\.$',
    14: r'(\w+) is an aunt of (\w+)\.$',
}

def parse_statement(input):
    for key, pattern in statements.items():
        match = re.match(pattern, input)
        if match:
            groups = match.groups()
            statement_key = key
            print(f"Statement: {groups}")
            process_statement(key, groups)
            return
    print("Input not recognized.")

def process_statement(key, names):
    if key == 1:
        soln = prolog.query(f"add_sibling({names[0]}, {names[1]})")
    elif key == 2:
        soln = prolog.query(f"add_sister({names[0]}, {names[1]})")
    elif key == 3:
        soln = prolog.query(f"add_mother({names[0]}, {names[1]})")
    elif key == 4:
        soln = prolog.query(f"add_grandmother({names[0]}, {names[1]})")
    elif key == 5:
        soln = prolog.query(f"add_child({names[0]}, {names[1]})")
    elif key == 6:
        soln = prolog.query(f"add_daughter({names[0]}, {names[1]})")
    elif key == 7:
        soln = prolog.query(f"add_uncle({names[0]}, {names[1]})")
    elif key == 8:
        soln = prolog.query(f"add_brother({names[0]}, {names[1]})")
    elif key == 9:
        soln = prolog.query(f"add_father({names[0]}, {names[1]})")
    elif key == 10:
        children = re.findall(r'\b(?!and\b)\w+', names[0])
        for x in children:
            soln = prolog.query(f'add_child({x}, {names[1]})')
    elif key == 11:
        soln = prolog.query(f"add_parent({names[0]}, {names[2]})")
        soln = prolog.query(f"add_parent({names[1]}, {names[2]})")
    elif key == 12:
        soln = prolog.query(f"add_grandfather({names[0]}, {names[1]})")
    elif key == 13:
        soln = prolog.query(f"add_son({names[0]}, {names[1]})")
    elif key == 14:
        soln = prolog.query(f"add_aunt({names[0]}, {names[1]})")

    if check_query(soln):
        print("OK! I learned something.")
    else:
        print("That's impossible!")

def check_query(x):
    if list(x):
        return True
    else:
        return False

chat_input = input("Enter a statement: ")
parse_statement(chat_input)