import sys
import csv
import os
from urllib.request import urlopen
from recipe_scrapers import scrape_html

def main(url, csv_file):
    html = urlopen(url).read().decode("utf-8")  # retrieves the recipe webpage HTML
    scraper = scrape_html(html, org_url=url)

    columns = ['title', 'ingredients', 'directions', 'link', 'source', 'NER', 'site']
    title = scraper.title()
    ingredients = scraper.ingredients()
    directions = scraper.instructions()
    link = url
    source = 'Scraped'
    try:
        NER = scraper.keywords()
    except:
        NER = ''
    site = scraper.host()
    data = [title, ingredients, directions, link, source, NER, site]
    is_appending = os.path.exists(csv_file)
    with open(csv_file, 'a') as out_file:
        writer = csv.writer(out_file)
        if not is_appending:
            writer.writerow(columns)
        writer.writerow(data)

if __name__ == "__main__":
    if len(sys.argv) > 2:
        main(sys.argv[1], sys.argv[2])
    else:
        print("Error: Please provide a recipe url and csv to create or append to.")