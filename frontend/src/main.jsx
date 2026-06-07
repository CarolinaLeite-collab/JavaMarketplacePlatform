import '@mantine/core/styles.css';
import {MantineProvider} from '@mantine/core';
import {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import {AppProvider} from './context/AppProvider';
import App from './App';

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <AppProvider>
            <MantineProvider>
                <App />
            </MantineProvider>
        </AppProvider>
    </StrictMode>
);