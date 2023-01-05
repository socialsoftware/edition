lighthouse http://localhost:8000  --only-categories performance --preset=desktop  --view 
lighthouse http://localhost:9000/ldod-mfes/  --only-categories performance --preset=desktop  --view 

lighthouse http://localhost:8000/fragments  --only-categories performance --preset=desktop  --view 
lighthouse http://localhost:9000/ldod-mfes/text/fragments  --only-categories performance --preset=desktop  --view 


lighthouse http://localhost:8000  --only-categories performance  --view 
lighthouse http://localhost:9000/ldod-mfes/  --only-categories performance  --view 

lighthouse http://localhost:8000/fragments  --only-categories performance  --view 
lighthouse http://localhost:9000/ldod-mfes/text/fragments  --only-categories performance   --view 

#lighthouse http://localhost:8080/ldod-mfes/text/fragments  --only-categories performance --preset=desktop --view --screenEmulation.width=1920 --screenEmulation.height=1080