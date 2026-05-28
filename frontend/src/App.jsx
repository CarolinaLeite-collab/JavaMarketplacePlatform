import { MantineProvider } from '@mantine/core';
import { BrowserRouter } from 'react-router-dom';
import { AppRoutes } from './routes/AppRoutes';
import '@mantine/core/styles.css';

export default function App() {
  return (
      <MantineProvider defaultColorScheme="auto">
        <BrowserRouter>
              <AppRoutes />
        </BrowserRouter>
      </MantineProvider>
  );
}