#!/bin/bash

ids="1_index 2_about 3_fragments 4_edition 5_ve"
urls_mfa="/ /about/faq /text/fragments /virtual/edition/acronym/LdoD-Arquivo /virtual/virtual-editions"
urls_jsp="/"
audits="first-contentful-paint speed-index total-blocking-time largest-contentful-paint cumulative-layout-shift total-byte-weight"
counter=0
for url in $urls_mfa
do
    id=$(echo "$ids" | awk -v counter="$counter" '{split($0,a," "); print a[counter+1]}')

    lighthouse \
        "http://localhost:8080/ldod-mfes$url" \
        --only-audits $audits \
        --preset=desktop \
        --view \
        --screenEmulation.disabled \
        --disable-full-page-screenshot \
        --output json \
        --quiet \
        --output-path "./report/mfa-desktop-$id.json"

    counter=$((counter + 1))
done
counter=0

for url in $urls_mfa
do
    id=$(echo "$ids" | awk -v counter="$counter" '{split($0,a," "); print a[counter+1]}')

    lighthouse \
        "http://localhost:8080/ldod-mfes$url" \
        --only-audits $audits \
        --view \
        --screenEmulation.disabled \
        --disable-full-page-screenshot \
        --output json \
        --quiet \
        --output-path "./report/mfa-mobile-$id.json"

    counter=$((counter + 1))
done
counter=0


for url in $urls_jsp
do
    id=$(echo "$ids" | awk -v counter="$counter" '{split($0,a," "); print a[counter+1]}')

    lighthouse \
        "http://localhost:8080$url" \
        --only-audits $audits \
        --preset=desktop \
        --view \
        --screenEmulation.disabled \
        --disable-full-page-screenshot \
        --output json \
        --quiet \
        --output-path "./report/jsp-desktop-$id.json"

    counter=$((counter + 1))
done
counter=0

for url in $urls_jsp
do
    id=$(echo "$ids" | awk -v counter="$counter" '{split($0,a," "); print a[counter+1]}')

    lighthouse \
        "http://localhost:8080$url" \
        --only-audits $audits \
        --view \
        --screenEmulation.disabled \
        --disable-full-page-screenshot \
        --output json \
        --quiet \
        --output-path "./report/jsp-mobile-$id.json"

    counter=$((counter + 1))
done

for url in $urls_jsp
do
    id=$(echo "$ids" | awk -v counter="$counter" '{split($0,a," "); print a[counter+1]}')

    lighthouse \
        "https://ldod.uc.pt/microfrontend$url" \
        --only-audits $audits \
        --view \
        --screenEmulation.disabled \
        --disable-full-page-screenshot \
        --output json \
        --quiet \
        --output-path "./report/react-mobile-$id.json"

    counter=$((counter + 1))
done
counter=0
for url in $urls_jsp
do
    id=$(echo "$ids" | awk -v counter="$counter" '{split($0,a," "); print a[counter+1]}')

    lighthouse \
        "https://ldod.uc.pt/microfrontend$url" \
        --only-audits $audits \
        --preset=desktop \
        --view \
        --screenEmulation.disabled \
        --disable-full-page-screenshot \
        --output json \
        --quiet \
        --output-path "./report/react-desktop-$id.json"

    counter=$((counter + 1))
done