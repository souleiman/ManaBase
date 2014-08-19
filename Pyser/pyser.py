__author__ = 'Souleiman'

import sys
import urllib.request
import json
import sqlite3
import re


def setup_card(card):
    c_type = ' '.join([str(x) for x in card['types']])

    for opt in ['manaCost', 'text', 'flavor']:
        if opt in card:
            card[opt] = re.sub('[\{\}]', '', card[opt])
            card[opt] = re.sub('[\n]', ' ', card[opt])

    db_card = dict()
    db_card['id'] = card['multiverseid']
    db_card['ascii'] = card['name']
    db_card['name'] = card['name']
    db_card['power'] = card['power'] if 'power' in card else None
    db_card['toughness'] = card['toughness'] if 'toughness' in card else None
    db_card['mana'] = card['manaCost'] if 'manaCost' in card else None
    db_card['loyalty'] = card['loyalty'] if 'loyalty' in card else None
    db_card['link'] = None
    db_card['ability'] = None

    if 'Creature' in c_type:
        db_card['type'] = "{main_type} \u2014 {archetype}" \
            .format(main_type=c_type,
                    archetype=" ".join([str(arc) for arc in card['subtypes']]))
        db_card['ability'] = card['text'] if 'text' in card else card['flavor']
    elif 'Sorcery' in c_type or 'Instant' in c_type or 'Enchantment' == c_type:
        db_card['type'] = '{main_type}{0}'.format(
            " \u2014 {0}".format(' '.join(card['subtypes'])) if 'subtypes' in card else '',
            main_type=c_type)
        db_card['ability'] = card['text']
    elif 'Land' in c_type:
        db_card['type'] = '{main_type}{0}'.format(
            " \u2014 {0}".format(' '.join(card['subtypes'])) if 'subtypes' in card else '',
            main_type=c_type)
        if not 'supertypes' in card:
            db_card['ability'] = card['text']
    elif 'Artifact' == c_type:
        db_card["type"] = c_type
        db_card['ability'] = card['text'] if 'text' in card else card['flavor']
    elif 'Planeswalker' in c_type:
        db_card['type'] = '{main_type}{0}'.format(
            " \u2014 {0}".format(' '.join(card['subtypes'])) if 'subtypes' in card else '',
            main_type=c_type)

        db_card['ability'] = card['text']
    else:
        raise Exception("Unknown type: {0}".format(c_type))

    return db_card


def process(url):
    request = urllib.request.urlopen(url)
    json_string = request.read()
    obj = json.loads(json_string.decode('utf-8'))

    cards = obj['cards']
    db_conn = sqlite3.connect('../manabase.db')
    db_cur = db_conn.cursor()

    added = 0
    skipped = 0
    for i in cards:
        db_card = setup_card(i)
        try:
            db_cur.execute(
                'INSERT INTO'
                ' MAGIC(ID, ASCII, NAME, TYPE, ABILITY, MANA, POWER, TOUGHNESS, LOYALTY,'
                'LINK) VALUES (:id, :ascii, :name, :type, :ability, :mana, :power, '
                ':toughness, :loyalty, :link)',
                db_card)
            added += 1
        except sqlite3.IntegrityError:
            print("{name} is a duplicate... skipping.".format(name=db_card['name']))
            skipped += 1

    db_conn.commit()
    db_conn.close()

    print("\nSuccessfully added: {0} / {1}".format(str(added), str(added + skipped)))
    print("Successfully skipped: {0} / {1}".format(str(skipped), str(added + skipped)))

if len(sys.argv) != 2:
    print("Excepted a parameter! Please specify the url for parsing.")

process(str(sys.argv[1]))