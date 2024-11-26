import React from 'react';
import type { Seat } from '../types';

interface SeatLayoutProps {
  seats: Seat[];
  selectedSeats: string[];
  onSeatSelect: (seatId: string) => void;
}

export function SeatLayout({ seats, selectedSeats, onSeatSelect }: SeatLayoutProps) {
  const getStatusClass = (status: Seat['status'], id: string) => {
    if (selectedSeats.includes(id)) return 'bg-emerald-500 text-white';
    if (status === 'booked') return 'bg-gray-200 text-gray-400 cursor-not-allowed';
    return 'bg-white hover:bg-emerald-50';
  };

  return (
    <div className="p-8 bg-white rounded-lg shadow-md">
      <div className="w-full max-w-2xl mx-auto">
        <div className="mb-8 flex justify-center">
          <div className="w-3/4 h-2 bg-gray-300 rounded-lg" />
        </div>
        
        <div className="grid grid-cols-10 gap-2">
          {seats.map((seat) => (
            <button
              key={seat.id}
              disabled={seat.status === 'booked'}
              onClick={() => onSeatSelect(seat.id)}
              className={`
                aspect-square rounded border-2 border-gray-300 
                transition-colors text-sm font-medium
                ${getStatusClass(seat.status, seat.id)}
              `}
            >
              {seat.row}{seat.number}
            </button>
          ))}
        </div>

        <div className="mt-8 flex justify-center gap-6">
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 bg-white border-2 border-gray-300 rounded" />
            <span className="text-sm text-gray-600">Available</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 bg-emerald-500 rounded" />
            <span className="text-sm text-gray-600">Selected</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 bg-gray-200 rounded" />
            <span className="text-sm text-gray-600">Booked</span>
          </div>
        </div>
      </div>
    </div>
  );
}