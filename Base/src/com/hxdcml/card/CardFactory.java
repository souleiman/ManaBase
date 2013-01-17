package com.hxdcml.card;

/**
 * User: Souleiman Ayoub
 * Date: 12/23/12
 * Time: 2:06 PM
 */
public class CardFactory {

    /**
     * Due to the understanding of hierarchy in Computer Science (or Java in this case)
     * derived classes or object cannot be casted from their parent class,
     * such as B b = (B) a; where a is a reference object of A which is a parent of B. This
     * method will bypass this issue <u>BUT be aware that the object will be reassigned so
     * the original object will no longer have any changes by reference.</u>
     *
     * @param card The card that we want to use to *reference* from
     * @param c the class, usually it's sub-class that we want to create
     * @param <K> The sub-class object we are to expect as a return
     * @return an object that is a child of the class that inherits Body
     */
    public static <K> K  create(Body card, Class<K> c) {
        Object object = null;
        try {
            K k = c.newInstance();
            if (k instanceof Planeswalker)
                object = clone(card, new Planeswalker());
            else if (k instanceof Creature)
                object = clone(card, new Creature());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return c.cast(object);
    }

    /**
     * Clones the from object into the _to_ object.
     * @param from the object we are to clone from
     * @param to the object that will retrieve the clones
     * @return the new cloned object
     */
    private static Body clone(Body from, Body to) {
        to.setId(from.getId());
        to.setName(from.getName());
        to.setType(from.getType());
        to.setAbility(from.getAbility());
        to.setFlavor(from.getFlavor());
        to.setMana(from.getMana());
        to.setCost(from.getCost());
        to.setRuling(from.getRuling());
        to.setFormat(from.getFormat());
        to.setLink(from.getLink());
        to.setImage(from.getImage());
        return to;
    }
}
