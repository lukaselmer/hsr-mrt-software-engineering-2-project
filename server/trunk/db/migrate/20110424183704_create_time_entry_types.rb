class CreateTimeEntryTypes < ActiveRecord::Migration
  def self.up
    create_table :time_entry_types do |t|
      t.integer :ancestor
      t.text :description
      t.datetime :updated_at
      t.datetime :valid_until

      t.timestamps
    end
  end

  def self.down
    drop_table :time_entry_types
  end
end
