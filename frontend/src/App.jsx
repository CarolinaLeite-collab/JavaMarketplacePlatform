import { MantineProvider } from '@mantine/core';
import { Notifications } from '@mantine/notifications';
import { BrowserRouter } from 'react-router-dom';
import { AppRoutes } from './routes/AppRoutes';
import '@mantine/core/styles.css';
import '@mantine/notifications/styles.css';
import {AppProvider, LibraryProvider} from "@/context/AppProvider.jsx";

export default function App() {
  return (
      <MantineProvider defaultColorScheme="auto">
          <Notifications />
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