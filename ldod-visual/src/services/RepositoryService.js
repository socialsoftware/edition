import axios from 'axios';

export class RepositoryService {
  constructor(editionAcronym) {

    //this.axios.setHeader('accept-encoding', 'null');

    this.setAcronym = editionAcronym;

    this.config = {
      headers: {
        'Authorization': "bearer" + localStorage.getItem('accessToken')
      }
    };

    this.bodyParameters = {
      key: "value"
    }

    this.axios = axios.create({
      baseURL: 'https://ldod.uc.pt',
      //baseURL: 'http://localhost:8080',
      timeout: 100000,
      headers: {
        'X-Custom-Header': 'BlendedWorkflow',
        'Authorization': 'Bearer' + localStorage.getItem('accessToken'),
        'Content-type': 'application/json;charset=utf-8',
        'responseEncoding': 'utf8'
      }
    });

    this.state = {
      //acronym: "LdoD-Arquivo"
      //acronym: "LdoD-test"
      //acronym: "LdoD-Twitter"
      //acronym: "LdoD-ReCritica" p testar word cloud com menos items
      acronym: "LdoD-Mallet"
    };
  }

  // Specifications
  getFragments() {
    return this.axios.get("/virtualeditions/acronym/" + this.setAcronym + "/fragments");
  }

  getTranscriptions() {
    return this.axios.get("/virtualeditions/acronym/" + this.setAcronym + "/transcriptions");
  }

  getIntersByDistance(interId, heteronym, text, date, taxonomy) {
    return this.axios.post('/recommendation/' + interId + '/intersByDistance', {
      'heteronymWeight': heteronym,
      'textWeight': text,
      'dateWeight': date,
      'taxonomyWeight': taxonomy
    });
  }

  //http://localhost:8080/virtualeditions/acronym/LdoD-test/interId/281861523767368/tfidf
  getFragmentTfIdf(interId) {
    return this.axios.get("/virtualeditions/acronym/" + this.setAcronym + "/interId/" + interId + "/tfidf");
  }

  //http://localhost:8080/virtualeditions/public
  //https://ldod.uc.pt/virtualeditions/public/
  getPublicEditions() {
    return this.axios.get("/virtualeditions/public/");
  }

}
