import { render, screen } from '../test-utils';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import { EditionStep } from '../components/addItemModal/EditionStep';

describe('EditionStep', () => {
    const data = {
        publicationTypeId: '',
        publishingCompanyId: '',
        publishingYear: 0,
        language: '',
        identifier: '',
        dimension: {
            width: 0,
            height: 0,
            thickness: 0,
            unit: '',
        },
        weight: {
            value: 0,
            unit: '',
        },
        numberOfPages: 0,
        editionNumber: 0,
        binding: '',
    };

    const publicationTypes = [
        { value: 'TYPE-001', label: 'Book' },
    ];

    const publishingCompanies = [
        { value: 'COMPANY-001', label: 'Prentice Hall' },
    ];

    const renderEditionStep = (setData = vi.fn()) => {
        render(
            <EditionStep
                data={data}
                setData={setData}
                publicationTypes={publicationTypes}
                publishingCompanies={publishingCompanies}
            />
        );

        return { setData };
    };

    it('renders all edition fields', () => {
        renderEditionStep();

        expect(screen.getByRole('combobox', { name: /publication type/i })).toBeInTheDocument();
        expect(screen.getByLabelText(/isbn\/issn/i)).toBeInTheDocument();
        expect(screen.getByRole('combobox', { name: /language/i })).toBeInTheDocument();
        expect(screen.getByRole('combobox', { name: /publishing company/i })).toBeInTheDocument();
        expect(screen.getByLabelText(/publishing year/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/^weight$/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/weight unit/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/width/i)).toBeInTheDocument();
        expect(screen.getByRole('combobox', { name: /binding/i })).toBeInTheDocument();
        expect(screen.getByLabelText(/number of pages/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/edition number/i)).toBeInTheDocument();
    });

    it('updates publication type when user selects an option', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.click(screen.getByRole('combobox', { name: /publication type/i }));
        await user.click(await screen.findByText('Book'));

        expect(setData).toHaveBeenCalled();
    });

    it('updates language when user selects an option', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.click(screen.getByRole('combobox', { name: /language/i }));
        await user.click(await screen.findByText('ENGLISH'));

        expect(setData).toHaveBeenCalled();
    });

    it('updates publishing company when user selects an option', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.click(screen.getByRole('combobox', { name: /publishing company/i }));
        await user.click(await screen.findByText('Prentice Hall'));

        expect(setData).toHaveBeenCalled();
    });

    it('updates identifier when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.type(screen.getByLabelText(/isbn\/issn/i), '9780132350884');

        expect(setData).toHaveBeenCalled();
    });

    it('updates publishing year when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.clear(screen.getByLabelText(/publishing year/i));
        await user.type(screen.getByLabelText(/publishing year/i), '2008');

        expect(setData).toHaveBeenCalled();
    });

    it('updates weight value when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.clear(screen.getByLabelText(/^weight$/i));
        await user.type(screen.getByLabelText(/^weight$/i), '800');

        expect(setData).toHaveBeenCalled();
    });

    it('updates weight unit when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.type(screen.getByLabelText(/weight unit/i), 'GRAMS');

        expect(setData).toHaveBeenCalled();
    });

    it('updates width when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.clear(screen.getByLabelText(/width/i));
        await user.type(screen.getByLabelText(/width/i), '23');

        expect(setData).toHaveBeenCalled();
    });

    it('updates binding when user selects an option', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.click(screen.getByRole('combobox', { name: /binding/i }));
        await user.click(await screen.findByText('PAPERBACK'));

        expect(setData).toHaveBeenCalled();
    });

    it('updates number of pages when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.clear(screen.getByLabelText(/number of pages/i));
        await user.type(screen.getByLabelText(/number of pages/i), '464');

        expect(setData).toHaveBeenCalled();
    });

    it('updates edition number when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderEditionStep(setData);

        await user.clear(screen.getByLabelText(/edition number/i));
        await user.type(screen.getByLabelText(/edition number/i), '1');

        expect(setData).toHaveBeenCalled();
    });
});