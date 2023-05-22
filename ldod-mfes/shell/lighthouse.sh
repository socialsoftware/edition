lighthouse \
    http://localhost:8080/ldod-mfes/ \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mfe-index.json

lighthouse \
    http://localhost:8080/ \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mono-index.json

lighthouse \
    http://localhost:8080/ldod-mfes/text/fragments \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mfe-frags.json

lighthouse \
    http://localhost:8080/fragments \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mono-frags.json
lighthouse \
   http://localhost:8080/ldod-mfes/virtual/edition/acronym/LdoD-Arquivo \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mfe-edition.json

lighthouse \
    http://localhost:8080/edition/acronym/LdoD-Arquivo \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mono-edition.json


lighthouse \
   http://localhost:8080/ldod-mfes/virtual/virtual-editions \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mfe-ve.json

lighthouse \
    http://localhost:8080/virtualeditions \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mono-ve.json

lighthouse \
  http://localhost:8080/ldod-mfes/about/faq \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mfe-faq.json

lighthouse \
    http://localhost:8080/about/faq \
    --only-categories performance \
    --preset=desktop \
    --view \
    --screenEmulation.disabled \
    --disable-full-page-screenshot \
    --output json \
    --quiet \
    --output-path ./report/mono-faq.json


#lighthouse http://localhost:8080/ldod-mfes/virtual/virtual-editions  --output=csv --only-categories performance --preset=desktop  --view --screenEmulation.disabled
#
#lighthouse http://localhost:8080/virtualeditions --output=csv  --only-categories performance --preset=desktop  --view --screenEmulation.disabled 
#


#lighthouse http://localhost:8000/fragments  --only-categories performance --preset=desktop  --view 
#lighthouse http://localhost:9000/ldod-mfes/text/fragments  --only-categories performance --preset=desktop  --view 
#
#
#lighthouse http://localhost:8000  --only-categories performance  --view 
#lighthouse http://localhost:9000/ldod-mfes/  --only-categories performance  --view 
#
#lighthouse http://localhost:8000/fragments  --only-categories performance  --view 
#lighthouse http://localhost:9000/ldod-mfes/text/fragments  --only-categories performance   --view 
#
#lighthouse http://localhost:8080/ldod-mfes/text/fragments  --only-categories performance --preset=desktop --view --screenEmulation.width=1920 --screenEmulation.height=1080