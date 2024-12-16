Python Web Scraper

This is a utility intended to support gathering more recipes for our recipe search program.

Requirements:

This requires Python 3.12 or later and the following modules all available on pip:
sys, csv, os, urllib, and recipe_scrapers (https://github.com/hhursev/recipe-scrapers/tree/main)

Limitations:

This does not work with every recipe site. In the time available adding automated scraping of search results and allowing for any recipe website wasn't possible but the underlying module handles many of the popular sites which can be viewed on their github page linked above.

How to run:

Note: If the csv file specified exists it will be appended to so ensure you have appropriate write permissions.

python scraper.py [url_of_recipe] [path/to/file.csv]
ex: python scraper.py https://www.cookwell.com/recipe/marcella-hazan-s-3-ingredient-tomato-sauce home/user/hadoop_project/input/tomato_sauces.csv
