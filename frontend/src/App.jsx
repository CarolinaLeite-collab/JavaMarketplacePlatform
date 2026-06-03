import { MantineProvider } from '@mantine/core';
import { BrowserRouter } from 'react-router-dom';
import { AppRoutes } from './routes/AppRoutes';
import '@mantine/core/styles.css';
import {AppProvider, LibraryProvider} from "@/context/AppProvider.jsx";

export default function App() {
  return (
      <MantineProvider defaultColorScheme="auto">
          <BrowserRouter>
              <AppProvider>
                  <LibraryProvider>
                      <AppRoutes />
                  </LibraryProvider>
              </AppProvider>
          </BrowserRouter>
      </MantineProvider>
  );
}