import {MantineProvider} from '@mantine/core';
import {Notifications} from '@mantine/notifications';
import {BrowserRouter} from 'react-router-dom';
import {AppRoutes} from './routes/AppRoutes';
import '@mantine/core/styles.css';
import '@mantine/notifications/styles.css';
import {AppProvider, LibraryProvider} from "@/context/AppProvider.jsx";
import {UserProvider} from "@/context/UserContext";

export default function App() {
  return (
      <MantineProvider defaultColorScheme="auto">
          <Notifications position="top-center"/>
              <BrowserRouter>
                  <UserProvider>
                      <AppProvider>
                          <LibraryProvider>
                              <AppRoutes />
                          </LibraryProvider>
                      </AppProvider>
                  </UserProvider>
              </BrowserRouter>
      </MantineProvider>
  );
}