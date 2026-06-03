import { Header } from '../header/Header';
import { Footer } from '../footer/Footer';
import { useComputedColorScheme } from '@mantine/core';
import { PageContent } from "./PageContent.tsx";

export function DefaultLayout({ title, subtitle, children }) {
    const colorScheme = useComputedColorScheme('light');

    return (
        <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
            <Header />
            <div data-testid="bg-wrapper"
             style={{
                flex: 1,
                backgroundColor: colorScheme === 'light'
                    ? 'var(--mantine-color-gray-1)'
                    : 'var(--mantine-color-dark-6)'
            }}>
                <PageContent title={title} subtitle={subtitle}>{children}</PageContent>
            </div>
            <Footer />
        </div>
    );
}