#!/bin/bash
kubectl apply -f deployment/frontend/docker-front-end.yaml
sleep 15
kubectl apply -f deployment/frontend/ingress.yaml
exit 0