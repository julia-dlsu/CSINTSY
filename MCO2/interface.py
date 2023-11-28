import re
import os
from pyswip import Prolog
from pyswip.easy import Variable
prolog = Prolog()
prolog.consult("logic.pl")
os.system('cls')

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
    14: r'(\w+) is an aunt of (\w+)\.$'
}

#format
#key: [pattern, type]
#qtype =
#0: true or false question || 1: enumeration
questions = {
    1: [r'are (\w+) and (\w+) siblings\?$', 0],
    2: [r'is (\w+) a sister of (\w+)\?$', 0] ,
    3: [r'is (\w+) a brother of (\w+)\?$', 0],
    4: [r'is (\w+) the mother of (\w+)\?$', 0],
    5: [r'is (\w+) the father of (\w+)\?$', 0],
    6: [r'are (\w+) and (\w+) the parents of (\w+)\?$', 0],
    7: [r'is (\w+) a grandmother of (\w+)\?$', 0],
    8: [r'is (\w+) a daughter of (\w+)\?$', 0],
    9: [r'is (\w+) a son of (\w+)\?$', 0],
    10: [r'is (\w+) a child of (\w+)\?$', 0],
    11: [r'are (.+?) children of (\w+)\?$', 0],
    12: [r'is (\w+) an uncle of (\w+)\?$', 0],
    13: [r'who are the siblings of (\w+)\?$', 1],
    14: [r'who are the sisters of (\w+)\?$', 1],
    15: [r'who are the brothers of (\w+)\?$', 1],
    16: [r'who is the mother of (\w+)\?$', 1],
    17: [r'who is the father of (\w+)\?$', 1],
    18: [r'who are the parents of (\w+)\?$', 1],
    19: [r'is (\w+) a grandfather of (\w+)\?$', 0],
    20: [r'who are the daughters of (\w+)\?$', 1],
    21: [r'who are the sons of (\w+)\?$', 1],
    22: [r'who are the children of (\w+)\?$', 1],
    23: [r'is (\w+) an aunt of (\w+)\?$', 0],
    24: [r'are (\w+) and (\w+) relatives\?$', 0]
}

def parse_statement(input):
    for key, pattern in statements.items():
        match = re.match(pattern, input, flags=re.IGNORECASE)
        if match:
            groups = match.groups()
            process_statement(key, groups)
            return True
    return False

def parse_question(input):
    for key, val in questions.items():
        match = re.match(val[0], input, flags=re.IGNORECASE)
        if match:
            groups = match.groups()
            process_question(key, groups, val[1])
            return True
    return False

def process_question(key, names, qtype):
    if key == 1:
        soln = prolog.query(f"sibling_of({names[0]}, {names[1]})")
    elif key == 2:
        soln = prolog.query(f"sister_of({names[0]}, {names[1]})")
    elif key == 3:
        soln = prolog.query(f"brother_of({names[0]}, {names[1]})")
    elif key == 4:
        soln = prolog.query(f"mother_of({names[0]}, {names[1]})")
    elif key == 5:
        soln = prolog.query(f"father_of({names[0]}, {names[1]})")
    elif key == 6:
        soln0 = prolog.query(f"parent_of({names[0]}, {names[2]})")
        soln1 = prolog.query(f"parent_of({names[1]}, {names[2]})")
    elif key == 7:
        soln = prolog.query(f"grandmother_of({names[0]}, {names[1]})")
    elif key == 8:
        soln = prolog.query(f"daughter_of({names[0]}, {names[1]})")
    elif key == 9:
        soln = prolog.query(f"son_of({names[0]}, {names[1]})")
    elif key == 10:
        soln = prolog.query(f"child_of({names[0]}, {names[1]})")
    elif key == 11:
        children = re.findall(r'\b(?!and\b)\w+', names[0])
        count = 0
        for x in children:
            soln = prolog.query(f"child_of({x}, {names[1]})")
            soln = list(soln)
            if check_query(soln): count += 1
        if count == len(children): print("Yes.")
        else: print("No.")
    elif key == 12:
        soln = prolog.query(f"uncle_of({names[0]}, {names[1]})")
    elif key == 13:
        query = f"sibling_of(X, {names[0]})"
    elif key == 14:
        query = f"sister_of(X, {names[0]})"
    elif key == 15:
        query = f"brother_of(X, {names[0]})"
    elif key == 16:
        query = f"mother_of(X, {names[0]})"
    elif key == 17:
        query = f"father_of(X, {names[0]})"
    elif key == 18:
        query = f"parent_of(X, {names[0]})"
    elif key == 19:
        soln = prolog.query(f"grandfather_of({names[0]}, {names[1]})")
    elif key == 20:
        query = f"daugher_of(X, {names[0]})"
    elif key == 21:
        query = f"son_of(X, {names[0]})"
    elif key == 22:
        query = f"child_of(X, {names[0]})"
    elif key == 23:
        soln = prolog.query(f"aunt_of({names[0]}, {names[1]})")
    elif key == 24:
        soln = prolog.query(f"relative_of({names[0]}, {names[1]})")
        
    if qtype == 0 and key != 11:
        if check_query(soln):
            print("Yes!")
        else:
            print("No!")
    elif qtype == 1:
        enumerate_query(query)

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
        wrong = []
        count = 0
        for x in children:
            soln = prolog.query(f'add_child({x}, {names[1]})')
            soln = list(soln)
            if check_query(soln): count += 1
            else: wrong.append(x)
        if count == len(children): print("OK! I learned something.")
        elif count > 0 and count < len(children): print(" and ".join(wrong), " cannot be added as children.")
        else: print("That's impossible!")
    elif key == 11:
        parents = [names[0], names[1]]
        wrong = []
        count = 0
        for x in parents:
            soln = prolog.query(f"add_parent({x}, {names[2]})")
            soln = list(soln)
            if check_query(soln): count += 1
            else: wrong.append(x)
        if count == len(parents): print("OK! I learned something.")
        elif count > 0 and count < len(parents): print(f"{wrong[0]} cannot be added as a parent.")
        else: print("That's impossible!")
    elif key == 12:
        soln = prolog.query(f"add_grandfather({names[0]}, {names[1]})")
    elif key == 13:
        soln = prolog.query(f"add_son({names[0]}, {names[1]})")
    elif key == 14:
        soln = prolog.query(f"add_aunt({names[0]}, {names[1]})")

    if check_query(soln) and key != 10 and key != 11:
        print("OK! I learned something.")
    elif not(check_query(soln)) and key != 10:
        print("That's impossible!")

def check_query(x):
    if list(x): return True
    return False

def enumerate_query(query):
    soln = prolog.query(query)
    prev = []
    if check_query(soln):
        for soln in prolog.query(query):
            if soln["X"] not in prev:
                print(soln["X"].capitalize())
            prev.append(soln["X"])
    else:
        print("There is no valid person for that category.")

def take_input():
    while(True):
        chat_input = input("> ")
        if chat_input == "exit":
            exit()
        else:
            check_statement = parse_statement(chat_input.lower())
            if not check_statement:
                check_question = parse_question(chat_input.lower())
            if not check_statement and not check_question:
                print("[Error] Invalid input.")

if __name__ == "__main__":
    print("[Expert System Prototype: Family Tree Chatbot]")
    print("Authors: Choa, de Veyra, Fortiz\n")
    take_input()