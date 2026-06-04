package Pairing;

public class Food {
    private final String name;
    private final String whiskyName;
    private final String recipe;
    private final String liqueur;
    private final String drink;
    private final String garnish;
    private final String extraIngredient;

    public Food(
            String name,
            String whiskyName,
            String recipe,
            String liqueur,
            String drink,
            String garnish,
            String extraIngredient
    ) {
        this.name = name;
        this.whiskyName = whiskyName;
        this.recipe = recipe;
        this.liqueur = liqueur;
        this.drink = drink;
        this.garnish = garnish;
        this.extraIngredient = extraIngredient;
    }

    public String getName() {
        return name;
    }

    public String getWhiskyName() {
        return whiskyName;
    }

    public String getRecipe() {
        return recipe;
    }

    public String getLiqueur() {
        return liqueur;
    }

    public String getDrink() {
        return drink;
    }

    public String getGarnish() {
        return garnish;
    }

    public String getExtraIngredient() {
        return extraIngredient;
    }
}
