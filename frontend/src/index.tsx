import React from 'react';
import ReactDOM from 'react-dom/client';
import './index/css/index.css';
import App from './index/App';

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);
root.render(
    <React.StrictMode>
        <App/>
    </React.StrictMode>
);
