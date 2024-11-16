import React from 'react';
import type { Show } from '../types';

interface ShowTimingsProps {
  shows: Show[];
  onSelectShow: (show: Show) => void;
}

export function ShowTimings({ shows, onSelectShow }: ShowTimingsProps) {
  return (
    <div className="flex flex-wrap gap-3">
      {shows.map((show) => (
        <button
          key={show.id}
          onClick={() => onSelectShow(show)}
          className="px-4 py-2 text-sm font-medium rounded-md border-2 border-emerald-500 text-emerald-700 hover:bg-emerald-50 transition-colors"
        >
          {show.time}
          <span className="block text-xs text-gray-500">₹{show.price}</span>
        </button>
      ))}
    </div>
  );
}