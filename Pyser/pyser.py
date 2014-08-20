"""
Usage: pyser -u <url>

-u, --url   The resource URL
"""
__author__ = 'Souleiman'

import sqlite3
import re

import requests
from docopt import docopt

def setup_card(card):
    c_type = ' '.join([str(x) for x in card['types']])

    for opt in ['manaCost', 'text', 'flavor']:
        if opt in card:
            card[opt] = re.sub(r'[{}]', '', card[opt])
            card[opt] = card[opt].replace('\n', ' ')

    db_card = {'id': card['multiverseid'], 'ascii': card['name'], 'name': card['name'],
               'power': card['power'] if 'power' in card else None,
               'toughness': card['toughness'] if 'toughness' in card else None,
               'mana': card['manaCost'] if 'manaCost' in card else None,
               'loyalty': card['loyalty'] if 'loyalty' in card else None, 'link': None, 'ability': None}

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


def process_manabase_resource(url):
    response = requests.get(url)
    cardset = response.json()
    db_conn = sqlite3.connect('../manabase.db')
    db_cur = db_conn.cursor()

    cards = cardset['cards']
    added = 0
    skipped = 0
    for card in cards:
        db_card = setup_card(card)
        try:
            db_cur.execute("""
                INSERT INTO
                 MAGIC(ID, ASCII, NAME, TYPE, ABILITY, MANA, POWER, TOUGHNESS, LOYALTY,
                LINK) VALUES (:id, :ascii, :name, :type, :ability, :mana, :power,
                :toughness, :loyalty, :link)
                """, db_card)
            added += 1
        except sqlite3.IntegrityError:
            print('{} is a duplicate... skipping.'.format(db_card['name']))
            skipped += 1

    db_conn.commit()
    db_conn.close()

    print('\nSuccessfully added: {} / {}'.format(str(added), str(added + skipped)))
    print('Successfully skipped: {} / {}'.format(str(skipped), str(added + skipped)))


def main():
    arguments = docopt(__doc__, version='Pyser 0.1')
    process_manabase_resource(arguments['<url>'])


if __name__ == '__main__':
    main()
