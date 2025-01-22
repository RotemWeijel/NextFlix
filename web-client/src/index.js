import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import BrowseScreen from './screens/main/BrowseScreen';
import PlayerScreen from './screens/main/PlayerScreen'
import reportWebVitals from './reportWebVitals';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MovieDetailsScreen from './screens/main/MovieDetailsScreen'

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route index element={<BrowseScreen tokenUser={'97f3f30c7512f8f507057e9c5752256a'} />} />
        <Route path="/MovieDetails/:movie" element={<MovieDetailsScreen tokenUser={'97f3f30c7512f8f507057e9c5752256a'} />} />
        <Route path='/Player/:movie' element={<PlayerScreen />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

reportWebVitals();
