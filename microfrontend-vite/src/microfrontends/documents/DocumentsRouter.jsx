import { lazy, useState, useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import { getLanguage, useStore } from '../../store';
import messages from './resources/constants';
import './resources/documents.css';

const SourceList = lazy(() => import('./pages/SourceList'));
const Fragments = lazy(() => import('./pages/Fragments'));
const language = state => state.language;

export default () => {

  return (
    <div className="container">
      <Routes>
        <Route path="/source/list" element={<SourceList  messages={messages} language={useStore(language)}/>} />
        <Route path="/fragments/*" element={<Fragments messages={messages} language={useStore(language)}/>} />
      </Routes>
    </div>
  );
};
