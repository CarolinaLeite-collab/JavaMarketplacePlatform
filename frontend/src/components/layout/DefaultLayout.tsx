import { Header } from '../header/Header';
import { Footer } from '../footer/Footer';
import { useComputedColorScheme } from '@mantine/core';
import { PageContent } from "./PageContent.tsx";

export function DefaultLayout({ title, children }) {
    const colorScheme = useComputedColorScheme('light');

    return (
        <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
            <Header />
            <div style={{
                flex: 1,
                backgroundColor: colorScheme === 'light'
                    ? 'var(--mantine-color-gray-1)'
                    : 'var(--mantine-color-dark-6)'
            }}>
                <PageContent title={title}>{children}</PageContent>
            </div>
            <Footer />
        </div>
    );
}