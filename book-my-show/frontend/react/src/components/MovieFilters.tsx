import React from 'react';
import { Filter } from 'lucide-react';

interface FilterOption {
  id: string;
  label: string;
}

interface MovieFiltersProps {
  languages: FilterOption[];
  genres: FilterOption[];
  selectedLanguages: string[];
  selectedGenres: string[];
  onLanguageChange: (language: string) => void;
  onGenreChange: (genre: string) => void;
  onClearFilters: () => void;
}

export function MovieFilters({
  languages,
  genres,
  selectedLanguages,
  selectedGenres,
  onLanguageChange,
  onGenreChange,
  onClearFilters
}: MovieFiltersProps) {
  return (
    <div className="w-64 bg-white rounded-lg shadow-md p-4">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Filter className="w-5 h-5 text-gray-600" />
          <h3 className="font-semibold text-gray-900">Filters</h3>
        </div>
        {(selectedLanguages.length > 0 || selectedGenres.length > 0) && (
          <button
            onClick={onClearFilters}
            className="text-sm text-rose-600 hover:text-rose-700"
          >
            Clear all
          </button>
        )}
      </div>

      <div className="space-y-6">
        <div>
          <h4 className="text-sm font-medium text-gray-900 mb-2">Language</h4>
          <div className="space-y-2">
            {languages.map((language) => (
              <label key={language.id} className="flex items-center">
                <input
                  type="checkbox"
                  checked={selectedLanguages.includes(language.id)}
                  onChange={() => onLanguageChange(language.id)}
                  className="w-4 h-4 text-rose-600 border-gray-300 rounded focus:ring-rose-500"
                />
                <span className="ml-2 text-sm text-gray-600">{language.label}</span>
              </label>
            ))}
          </div>
        </div>

        <div>
          <h4 className="text-sm font-medium text-gray-900 mb-2">Genre</h4>
          <div className="space-y-2">
            {genres.map((genre) => (
              <label key={genre.id} className="flex items-center">
                <input
                  type="checkbox"
                  checked={selectedGenres.includes(genre.id)}
                  onChange={() => onGenreChange(genre.id)}
                  className="w-4 h-4 text-rose-600 border-gray-300 rounded focus:ring-rose-500"
                />
                <span className="ml-2 text-sm text-gray-600">{genre.label}</span>
              </label>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}