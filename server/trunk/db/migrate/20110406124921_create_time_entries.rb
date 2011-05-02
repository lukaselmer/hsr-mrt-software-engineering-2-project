class CreateTimeEntries < ActiveRecord::Migration
  def self.up
    create_table :time_entries do |t|
      t.references :customer
      t.references :time_entry_type
      t.references :user
      t.references :location
      t.references :order
      t.string :hashcode
      t.text :description
      t.timestamp :time_start
      t.timestamp :time_stop
      t.string :audio_record_name
      
      t.timestamps
    end
  end

  def self.down
    drop_table :time_entries
  end
end
