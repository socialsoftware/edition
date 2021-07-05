import './App.css';
import './App.css'
import Root from './microfrontends/Root';
import React from 'react'
import { createStore } from 'redux';
import { persistStore, persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage' // defaults to localStorage for web
import reducer from './util/redux/reducer';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';

function App() {

    //////////REDUX STORE
    const persistConfig = {
      key: 'root',
      storage,
  }
    const persistedReducer = persistReducer(persistConfig, reducer)
    const store = createStore(persistedReducer)
    const persistor = persistStore(store)
    ///////////


  return (
    <div className="App">
      <Provider store={store}>
        <PersistGate persistor={persistor}>
          <Root/>
        </PersistGate>
      </Provider>
    </div>
  );
}

export default App;
